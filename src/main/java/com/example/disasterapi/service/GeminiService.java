package com.example.disasterapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService(WebClient.Builder builder, ObjectMapper objectMapper) {
        this.webClient = builder.baseUrl("https://generativelanguage.googleapis.com/v1beta/models").build();
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a prompt to the Gemini API and returns a raw String response.
     * This method is suitable for conversational or unstructured responses.
     *
     * @param model The Gemini model to use (e.g., "gemini-2.0-flash").
     * @param prompt The user's prompt.
     * @return The raw String response from the API.
     */
    public String askGeminiRaw(String model, String prompt) {
        String url = String.format("/%s:generateContent?key=%s", model, apiKey);

        Map<String, Object> requestBody = Map.of(
            "contents", new Object[]{
                Map.of("role", "user", "parts", new Object[]{
                    Map.of("text", prompt)
                })
            }
        );

        return webClient.post()
            .uri(url)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> Mono.just("{\"error\":\"" + e.getMessage() + "\"}"))
            .block();
    }

    /**
     * Sends a prompt to the Gemini API and requests a structured JSON response based on a schema.
     * This method is more reliable for getting predictable JSON output from the model.
     *
     * @param model The Gemini model that supports structured output (e.g., "gemini-2.5-flash-preview-05-20").
     * @param prompt The user's prompt.
     * @param jsonSchema A String containing the JSON schema for the desired response.
     * @return A String containing the JSON response.
     * @throws JsonProcessingException if there's an issue parsing the provided JSON schema.
     */
    public String askGeminiStructured(String model, String prompt, String jsonSchema) throws JsonProcessingException {
        String url = String.format("/%s:generateContent?key=%s", model, apiKey);

        // Parse the JSON schema string into a JsonNode object
        JsonNode responseSchemaNode = objectMapper.readTree(jsonSchema);

        Map<String, Object> requestBody = Map.of(
            "contents", new Object[]{
                Map.of("role", "user", "parts", new Object[]{
                    Map.of("text", prompt)
                })
            },
            "generationConfig", Map.of(
                "responseMimeType", "application/json",
                "responseSchema", responseSchemaNode
            )
        );

        // The API returns a full response body, so we need to extract the JSON string.
        String rawResponse = webClient.post()
            .uri(url)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> Mono.just("{\"error\":\"" + e.getMessage() + "\"}"))
            .block();

        // Extract the JSON string from the nested response.
        JsonNode root = objectMapper.readTree(rawResponse);
        return root.at("/candidates/0/content/parts/0/text").asText();
    }
}
