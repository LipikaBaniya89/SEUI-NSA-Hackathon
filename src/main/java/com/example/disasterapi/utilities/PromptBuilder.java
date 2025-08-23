package com.example.disasterapi.utilities;

import com.example.disasterapi.model.DisasterSchema;
import com.example.disasterapi.model.HelpSchema;
import com.example.disasterapi.model.PersonProfile;
import com.example.disasterapi.model.Prompt;

public class PromptBuilder {

    public static Prompt buildDisasterHelpPrompt(PersonProfile profile, String disasterType) {
            String hobbies = profile.getHobbies() != null ? String.join(", ", profile.getHobbies()) : "none";
            String promptText = String.format(
                    "You are an expert disaster response assistant. A %d-year-old person " +
                            "living in %s, working as %s, and with hobbies like %s, wants to know " +
                            "how they can help during a %s. Respond with a JSON object listing actionable tasks " +
                            "that this person can safely perform. Do not include anything else.",
                    profile.getAge(),
                    profile.getAge(),
                    profile.getOccupation(),
                    hobbies,
                    disasterType
            );
            // Create a schema object
            DisasterSchema schema = new DisasterSchema();
            return new Prompt(promptText, null, schema);
        }
}
