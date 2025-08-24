package com.example.disasterapi.dto;

/**
 * A Data Transfer Object (DTO) to represent a single recommended task
 * for a person to help with during a disaster.
 *
 * This DTO will be used to map the JSON objects returned by the Gemini API.
 */
public class TaskRecommendation {

    private String taskDescription;
    private String detailedDescription;
    private String link;
    private String helpType;

    public TaskRecommendation() {
    }

    public TaskRecommendation(String taskDescription, String detailedDescription, String link, String helpType) {
        this.taskDescription = taskDescription;
        this.detailedDescription = detailedDescription;
        this.link = link;
        this.helpType = helpType;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public String getHelpType() {
        return helpType;
    }
    
    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }

    @Override
    public String toString() {
        return "TaskRecommendation{" +
               "taskDescription='" + taskDescription + '\'' +
               ", detailedDescription='" + detailedDescription + '\'' +
               ", link='" + link + '\'' +
               ", helpType='" + helpType + '\'' +
               '}';
    }
}
