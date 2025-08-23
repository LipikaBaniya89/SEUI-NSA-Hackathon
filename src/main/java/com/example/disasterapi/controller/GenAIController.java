package com.example.disasterapi.controller;
import com.example.disasterapi.model.Prompt;
import com.example.disasterapi.service.GenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class GenAIController {

    private final GenAIService genAISerivce;

    public GenAIController(GenAIService chatbotService) {
        this.genAISerivce = chatbotService;
    }

    @PostMapping("/request")
    public String request(@RequestBody Prompt prompt) throws Exception {
        return genAISerivce.request(prompt);
    }

    @PostMapping("/requestWithHistory")
    public String requestWithHistory(@RequestBody Prompt prompt) throws Exception {
        return genAISerivce.requestWithHistory(prompt);
    }

    @PostMapping("/clearHistory")
    public void clearHistory() {
        genAISerivce.clearHistory();
    }
}