package com.example.disasterapi.controller;

import com.example.disasterapi.dto.PersonInfo;
import com.example.disasterapi.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api")
public class HelpController {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public HelpController(GeminiService geminiService, ObjectMapper objectMapper) {
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    /**
     * Gets help information from the Gemini API for a person in an earthquake scenario.
     * This method is refactored to use the Gemini API's structured output to ensure a valid JSON response.
     * @param name The name of the person.
     * @param skill The person's skill.
     * @param location The person's current location.
     * @return A JSON string containing the person's name, location, and a helpful message.
     * @throws Exception if the API call or JSON processing fails.
     */
    @GetMapping("/helps-structured")
    public JsonNode getStructuredHelp(@RequestParam String name,
                                      @RequestParam String skill,
                                      @RequestParam String location) throws Exception {

        // The prompt asks for a generic help message related to an earthquake.
        String prompt = String.format(
            "Person: %s\nSkill: %s\nLocation: %s\n\nThere has been an earthquake. What can this person help with?",
            name, skill, location
        );

        // This is the JSON schema that we will request from the Gemini API.
        // It's a precise definition of the output structure.
        String jsonSchema = """
            {
              "type": "object",
              "properties": {
                "name": { "type": "string" },
                "location": { "type": "string" },
                "helpMessage": { "type": "string" }
              },
              "required": ["name", "location", "helpMessage"]
            }
            """;

        // We call a specialized service method that tells the Gemini API to use the
        // structured output.
        String jsonResponse = geminiService.askGeminiStructured(
            "gemini-2.5-flash-preview-05-20", // Use a model that supports structured output
            prompt,
            jsonSchema
        );

        // The response is guaranteed to be valid JSON, so we can parse it directly.
        return objectMapper.readTree(jsonResponse);
    }

    // The original method is kept here for comparison.
    // This approach relies on the model generating a valid JSON string without
    // explicit guidance, which is less reliable.
    /*
    @GetMapping("/helps")
    public String getHelpFromQuery(
            @RequestParam String name,
            @RequestParam String skill,
            @RequestParam String location) {

        String prompt = String.format(
                "Person: %s\nSkill: %s\nLocation: %s\n\n. There has been an earthquake. What can this person help with? Return response in JSON format with fields name, location, helpMessage.",
                name, skill, location
        );

        // Call Gemini
        return geminiService.askGemini("gemini-2.0-flash", prompt);
    }
    */
}
