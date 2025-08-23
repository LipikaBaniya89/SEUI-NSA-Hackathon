package com.example.disasterapi.service;

import com.example.disasterapi.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GenAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private String model = "gemini-2.5-flash-lite";

    private final String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models";

    private final List<Content> history = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    private String getFormattedUrl() {
        return String.format("%s/%s:generateContent?key=%s", baseUrl, model, apiKey);
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void clearHistory() {
        history.clear();
    }

    public String request(Prompt prompt) throws Exception {
        Request request = createDefaultRequest(prompt);
        addOptionalFields(request, prompt);

        String responseText = sendRequest(request);
        return responseText;
    }

    public String requestWithHistory(Prompt prompt) throws Exception {
        Content newUserContent = createNextContent(prompt);

        Request request = new Request();
        List<Content> combined = new ArrayList<>(history);
        combined.add(newUserContent);
        request.setContents(combined.toArray(new Content[0]));

        addOptionalFields(request, prompt);

        String responseText = sendRequest(request);

        // Add to history
        history.add(newUserContent);
        Content npcContent = new Content();
        npcContent.setParts(new Part[]{new Part() {{ setText(responseText); }}});
        history.add(npcContent);

        log.info("LLM History: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(history));

        return responseText;
    }

    private void addOptionalFields(Request request, Prompt prompt) {
        if (prompt.getSystemInstructions() != null && !prompt.getSystemInstructions().isEmpty()) {
            Content systemInstruction = new Content();
            systemInstruction.setRole("model");
            systemInstruction.setParts(new Part[]{new Part() {{ setText(prompt.getSystemInstructions()); }}});
            request.setSystemInstruction(systemInstruction);
        }

        if (prompt.getSchema() != null) {
            if (request.getGenerationConfig() == null) {
                request.setGenerationConfig(new GenerationConfig());
                request.getGenerationConfig().setResponseMimeType("application/json");
            }
            request.getGenerationConfig().setResponseSchema(prompt.getSchema());
        }
    }

    private Request createDefaultRequest(Prompt prompt) {
        Request request = new Request();
        Content content = new Content();
        content.setParts(new Part[]{new Part() {{ setText(prompt.getPrompt()); }}});
        request.setContents(new Content[]{content});
        return request;
    }

    private Content createNextContent(Prompt prompt) {
        Content content = new Content();
        content.setRole("user");
        content.setParts(new Part[]{new Part() {{ setText(prompt.getPrompt()); }}});
        return content;
    }

    private String sendRequest(Request request) throws Exception {
        String body = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(getFormattedUrl(), HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();

        Response response = objectMapper.readValue(responseBody, Response.class);
        if (response == null || response.getError() != null) {
            throw new RuntimeException("API Error: ");
        }

        return "success";
    }
}