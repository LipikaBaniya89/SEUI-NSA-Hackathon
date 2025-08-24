package com.example.disasterapi.controller;


import com.example.disasterapi.domain.AppRole;
import com.example.disasterapi.domain.AppUser;
import com.example.disasterapi.repo.AppUserRepository;
import com.example.disasterapi.security.JwtService;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

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
                .latitude(r.getLatitude()).longitude(r.getLongitude())
                .radiusMeters(r.getRadiusMeters() == null ? 10000 : r.getRadiusMeters())
                .skills(new HashSet<>(r.getSkills() == null ? List.of() : r.getSkills()))
                .phone(r.getPhone()).wantsEmail(Boolean.TRUE.equals(r.getWantsEmail())).wantsSms(Boolean.TRUE.equals(r.getWantsSms()))
                .build();
        users.save(u);
        String token = jwt.generate(u.getEmail(), Map.of("role", u.getRole().name(), "uid", u.getId()));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest r) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(r.getEmail(), r.getPassword()));
        String token = jwt.generate(r.getEmail(), Map.of("role", auth.getAuthorities().iterator().next().getAuthority(), "uid", "n/a"));
        return ResponseEntity.ok(Map.of("token", token));
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
        private Double latitude;
        @NotNull
        private Double longitude;
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
