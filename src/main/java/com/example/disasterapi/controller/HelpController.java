package com.example.disasterapi.controller;

import com.example.disasterapi.dto.PersonInfo;
import com.example.disasterapi.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelpController {

    private final GeminiService geminiService;

    public HelpController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // @PostMapping("/help")
    // public String getHelp(@RequestBody PersonInfo personInfo) {
    //     String prompt = String.format(
    //             "Person: %s\nSkill: %s\nLocation: %s\n\nWhat can this person help with? Return response in JSON format with fields name, location, helpMessage.",
    //             personInfo.getName(),
    //             personInfo.getSkill(),
    //             personInfo.getLocation()
    //     );

    //     // Call Gemini (you can pick "gemini-2.0-flash" or "gemini-2.5-flash-lite")
    //     return geminiService.askGemini("gemini-2.0-flash", prompt);
    // }
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

}
