package com.example.disasterapi.dto;

public class HelpResponse {
    private String name;
    private String location;
    private String helpMessage;

    public HelpResponse(String name, String location, String helpMessage) {
        this.name = name;
        this.location = location;
        this.helpMessage = helpMessage;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getHelpMessage() { return helpMessage; }
}
