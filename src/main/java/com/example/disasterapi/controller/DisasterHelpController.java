package com.example.disasterapi;

import com.example.disasterapi.dto.DisasterHelpResponse;
import com.example.disasterapi.dto.HelpRequest;
import com.example.disasterapi.service.DisasterService;
import com.example.disasterapi.service.GeminiService;
import com.example.disasterapi.utilities.PromptBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api1")
@CrossOrigin(origins = "*")
public class DisasterHelpController {

    private final GeminiService geminiService;
    private final DisasterService disasterService;

    public DisasterHelpController(GeminiService geminiService, DisasterService disasterService) {
        this.geminiService = geminiService;
        this.disasterService = disasterService;
    }

    /**
     * Provides a comprehensive disaster help response for a person via a POST request.
     * The person's details are sent in the request body.
     *
     * @param request The HelpRequest object containing the person's name, skill, location, and radius.
     * @return A DisasterHelpResponse object with disaster details and a list of prioritized tasks.
     * @throws Exception if there is an issue getting the data or parsing the response.
     */
    @PostMapping("/help")
    public DisasterHelpResponse getHelp(@RequestBody HelpRequest request) throws Exception {

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
}
