package com.example.disasterapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://generativelanguage.googleapis.com/v1beta/models").build();
    }

    public String askGemini(String model, String prompt) {
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
}
