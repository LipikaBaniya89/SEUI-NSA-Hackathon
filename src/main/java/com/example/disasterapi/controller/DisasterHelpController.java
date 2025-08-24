package com.example.disasterapi;

import com.example.disasterapi.dto.HelpResponse;
import com.example.disasterapi.service.DisasterService;
import com.example.disasterapi.service.GeminiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DisasterHelpController {

    private final GeminiService geminiService;
    private final DisasterService disasterService;

    public DisasterHelpController(GeminiService geminiService, DisasterService disasterService) {
        this.geminiService = geminiService;
        this.disasterService = disasterService;
    }

    @GetMapping("/help")
    public HelpResponse getHelp(@RequestParam String name,
                                @RequestParam String skill,
                                @RequestParam String location,
                                @RequestParam(defaultValue = "500") int radius) throws Exception {

        List<Map<String, Object>> disasters = disasterService.getDisasters(location, radius);

        ObjectMapper mapper = new ObjectMapper();
        String disastersJson = mapper.writeValueAsString(disasters);
    
        String prompt = String.format(
                "Person: %s\nSkill: %s\nLocation: %s\n\n" +
                "Nearby disasters: %s\n\n" +
                "Determine which disaster this person can help the most.\n" +
                "Return JSON ONLY in this format:\n" +
                "{ \"name\": \"string\", \"recommendedDisaster\": \"string\", \"helpMessage\": \"string\" }",
                name, skill, location, disastersJson
        );

        String rawResponse = geminiService.askGemini("gemini-2.0-flash", prompt);

        JsonNode root = mapper.readTree(rawResponse);
        String jsonString = root.at("/candidates/0/content/parts/0/text").asText();

        return mapper.readValue(jsonString, HelpResponse.class);
    }
}
