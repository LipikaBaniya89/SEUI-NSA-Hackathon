package com.example.disasterapi.dto;

public class HelpResponse {
    private String name;
    private String recommendedDisaster; // added
    private String helpMessage;

    public HelpResponse() {} // default constructor for Jackson

    public HelpResponse(String name, String recommendedDisaster, String helpMessage) {
        this.name = name;
        this.recommendedDisaster = recommendedDisaster;
        this.helpMessage = helpMessage;
    }

    public String getName() { return name; }
    public String getRecommendedDisaster() { return recommendedDisaster; }
    public String getHelpMessage() { return helpMessage; }

    public void setName(String name) { this.name = name; }
    public void setRecommendedDisaster(String recommendedDisaster) { this.recommendedDisaster = recommendedDisaster; }
    public void setHelpMessage(String helpMessage) { this.helpMessage = helpMessage; }
}
