package com.example.disasterapi.utilities;

public class PromptBuilder {

      /**
     * Builds a prompt for the Gemini API to determine the most suitable disaster for a person to help with.
     * The prompt includes the person's details and a list of nearby disasters. It now also requests
     * a detailed, step-by-step description and a help type (online or in-person) for each task.
     *
     * @param name The name of the person.
     * @param skill The person's skill.
     * @param location The person's current location.
     * @param disastersJson A JSON string of nearby disasters.
     * @return A formatted prompt string for the Gemini API.
     */
    public static String buildDisasterHelpPrompt(String name, String skill, String location, String disastersJson) {
        return String.format(
            "Person: %s\nSkill: %s\nLocation: %s\n\n" +
            "Nearby disasters: %s\n\n" +
            "Determine which disaster this person can help the most. Provide a prioritized list of the most valuable tasks to the least valuable tasks. Each item in the list should include a task description, a detailed step-by-step description of what needs to be done, a valid and helpful URL link to a relevant resource, and a help type (online or inperson).",
            name, skill, location, disastersJson
        );
    }
}
