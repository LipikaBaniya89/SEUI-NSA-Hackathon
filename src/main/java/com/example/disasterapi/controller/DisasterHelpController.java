package com.example.disasterapi;

import com.example.disasterapi.dto.DisasterHelpResponse;
import com.example.disasterapi.service.DisasterService;
import com.example.disasterapi.service.GeminiService;
import com.example.disasterapi.utilities.PromptBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
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

    /**
     * Provides a comprehensive disaster help response, including disaster details and
     * a prioritized list of tasks for a person to help with.
     *
     * @param name The name of the person.
     * @param skill The skill of the person.
     * @param location The current location of the person.
     * @param radius The search radius for nearby disasters in miles.
     * @return A DisasterHelpResponse object containing disaster information and a list of prioritized tasks.
     * @throws Exception if there is an issue getting the data or parsing the response.
     */
    @GetMapping("/help")
    public DisasterHelpResponse getHelp(@RequestParam String name,
                                        @RequestParam String skill,
                                        @RequestParam String location,
                                        @RequestParam(defaultValue = "500") int radius) throws Exception {

        // 1. Get nearby disasters. This part remains the same.
        List<Map<String, Object>> disasters = disasterService.getDisasters(location, radius);

        ObjectMapper mapper = new ObjectMapper();
        String disastersJson = mapper.writeValueAsString(disasters);

        // 2. Build the prompt using the centralized PromptBuilder, now with a request for URL links.
        String prompt = PromptBuilder.buildDisasterHelpPrompt(
            name, skill, location, disastersJson // true indicates a request for URL links
        );

        // 3. Define the desired JSON schema for the entire response object.
        // This schema now includes top-level properties for disaster details.
        String jsonSchema = """
            {
              "type": "object",
              "properties": {
                "disasterType": { "type": "string" },
                "peopleAffected": { "type": "number" },
                "disasterDetails": { "type": "string" },
                "recommendedDisaster": { "type": "string" },
                "helpTasks": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "properties": {
                      "taskDescription": { "type": "string" },
                      "detailedDescription": { "type": "string" },
                      "link": { "type": "string" },
                      "helpType": { "type": "string" }
                    },
                    "required": ["taskDescription", "detailedDescription", "link", "helpType"]
                  }
                }
              },
              "required": ["disasterType", "peopleAffected", "disasterDetails", "recommendedDisaster", "helpTasks"]
            }
            """;

        // 4. Call a new, specialized service method to get a structured JSON response.
        String jsonString = geminiService.askGeminiStructured(
            "gemini-2.5-flash-preview-05-20",
            prompt,
            jsonSchema
        );

        // 5. Directly map the guaranteed-to-be-valid JSON string to the new DTO.
        return mapper.readValue(jsonString, DisasterHelpResponse.class);
    }
}
