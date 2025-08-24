package com.example.disasterapi.controller;


import com.example.disasterapi.domain.AppRole;
import com.example.disasterapi.domain.AppUser;
import com.example.disasterapi.dto.DisasterHelpResponse;
import com.example.disasterapi.dto.HelpRequest;
import com.example.disasterapi.repo.AppUserRepository;
import com.example.disasterapi.security.JwtService;
import com.example.disasterapi.service.DisasterService;
import com.example.disasterapi.service.GeminiService;
import com.example.disasterapi.service.GeoResult;
import com.example.disasterapi.service.GeocodingService;
import com.example.disasterapi.utilities.PromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;
    private final GeocodingService geocodingService;
    private final GeminiService geminiService;
    private final DisasterService disasterService;



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest r) {
        log.info("SignUpRequest r {}",r.toString());
        if (users.findByEmail(r.getEmail()).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error", "email already exists"));
        AppUser u = AppUser.builder()
                .email(r.getEmail())
                .passwordHash(encoder.encode(r.getPassword()))
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .role(r.getRole() == null ? AppRole.USER : r.getRole()).enabled(true)
                .address(r.getAddress())
                .latitude(getGeoCode(r.getAddress()).lat())
                .longitude(getGeoCode(r.getAddress()).lon())
                .radiusMeters(r.getRadiusMeters() == null ? 10000 : r.getRadiusMeters())
                .skills(new HashSet<>(r.getSkills() == null ? List.of() : r.getSkills()))
                .phone(r.getPhone())
                .wantsEmail(Boolean.TRUE.equals(r.getWantsEmail()))
                .wantsSms(Boolean.TRUE.equals(r.getWantsSms()))
                .build();
        users.save(u);
        String token = jwt.generate(u.getEmail(), Map.of("role", u.getRole().name(), "uid", u.getId()));
        return ResponseEntity.ok(Map.of("token", token));
    }

    private GeoResult getGeoCode(String address) {
        String a = (address == null) ? "" : address.trim();
        if (a.isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        return geocodingService.geocode(a)
                .orElseThrow(() -> new IllegalArgumentException("Could not geocode address: " + a));
    }



    @PostMapping("/signin")
    public ResponseEntity<DisasterHelpResponse> signin(@RequestBody SigninRequest r) throws Exception {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(r.getEmail(), r.getPassword()));
        String token = jwt.generate(r.getEmail(), Map.of("role", auth.getAuthorities().iterator().next().getAuthority(), "uid", "n/a"));
        HelpRequest request = new HelpRequest();
        if(token != null){
            AppUser user = users.findByEmail(r.getEmail()).get();
            request.setLocation(user.getAddress());
            request.setRadius(user.getRadiusMeters());
            request.setSkill(String.join(",",user.getSkills()));
            request.setName(user.getFirstName()+" "+user.getLastName());

        }
        return ResponseEntity.ok(getHelp(request));
    }

    public DisasterHelpResponse getHelp( HelpRequest request) throws Exception {

        // 1. Get nearby disasters. This part remains the same.
        List<Map<String, Object>> disasters = disasterService.getDisasters(request.getLocation(), request.getRadius());

        ObjectMapper mapper = new ObjectMapper();
        String disastersJson = mapper.writeValueAsString(disasters);

        // 2. Build the prompt using the centralized PromptBuilder.
        String prompt = PromptBuilder.buildDisasterHelpPrompt(
                request.getName(), request.getSkill(), request.getLocation(), disastersJson
        );

        // 3. Define the desired JSON schema for the entire response object.
        String jsonSchema = """
{
  "type": "object",
  "properties": {
    "disasterType": { "type": "string" },
    "peopleAffected": { "type": "number" },
    "disasterDetails": { "type": "string" },
    "recommendedDisaster": { "type": "string" },
    "disasterIcon": { "type": "string", "description": "URL to an icon/image representing the disaster" },
    "helpTasks": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "taskDescription": { "type": "string" },
          "detailedDescription": { "type": "string" },
          "link": { "type": "string" },
          "helpType": { "type": "string" },
          "taskIcon": { "type": "string", "description": "URL to an icon/image representing this task" },
          "precautions": { 
            "type": "array", 
            "items": { "type": "string" },
            "description": "Precautionary steps or safety info for volunteers doing this task"
          },
          "steps": {
            "type": "array",
            "items": { "type": "string" },
            "description": "Step-by-step instructions for performing this task"
          }
        },
        "required": ["taskDescription", "detailedDescription", "link", "helpType", "taskIcon", "precautions", "steps"]
      }
    }
  },
  "required": ["disasterType", "peopleAffected", "disasterDetails", "recommendedDisaster", "helpTasks"]
}
""";


        // 4. Call a specialized service method to get a structured JSON response.
        String jsonString = geminiService.askGeminiStructured(
                "gemini-2.5-flash-preview-05-20",
                prompt,
                jsonSchema
        );

        // 5. Directly map the guaranteed-to-be-valid JSON string to the DTO.
        return mapper.readValue(jsonString, DisasterHelpResponse.class);
    }

    @Data
    public static class SignupRequest {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        private AppRole role;
        @NotNull
        private String address;
        private Integer radiusMeters;
        private List<String> skills;
        private String phone;
        private Boolean wantsEmail;
        private Boolean wantsSms;
    }

    @Data
    public static class SigninRequest {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }
}
