package com.example.disasterapi.dto;

import java.util.List;

/**
 * A Data Transfer Object (DTO) to represent a single recommended task
 * for a person to help with during a disaster, including icons, precautions, and step-by-step instructions.
 */
public class TaskRecommendation {

    private String taskDescription;
    private String detailedDescription;
    private String link;
    private String helpType;
    private String taskIcon; // URL to an icon/image representing this task
    private List<String> precautions; // Safety tips or precautionary steps
    private List<String> steps; // Step-by-step instructions for performing the task

    public TaskRecommendation() {
    }

    public TaskRecommendation(String taskDescription, String detailedDescription, String link, String helpType,
                              String taskIcon, List<String> precautions, List<String> steps) {
        this.taskDescription = taskDescription;
        this.detailedDescription = detailedDescription;
        this.link = link;
        this.helpType = helpType;
        this.taskIcon = taskIcon;
        this.precautions = precautions;
        this.steps = steps;
    }

    // Getters and Setters
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

    public String getTaskIcon() {
        return taskIcon;
    }

    public void setTaskIcon(String taskIcon) {
        this.taskIcon = taskIcon;
    }

    public List<String> getPrecautions() {
        return precautions;
    }

    public void setPrecautions(List<String> precautions) {
        this.precautions = precautions;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "TaskRecommendation{" +
               "taskDescription='" + taskDescription + '\'' +
               ", detailedDescription='" + detailedDescription + '\'' +
               ", link='" + link + '\'' +
               ", helpType='" + helpType + '\'' +
               ", taskIcon='" + taskIcon + '\'' +
               ", precautions=" + precautions +
               ", steps=" + steps +
               '}';
    }
}
