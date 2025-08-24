package com.example.disasterapi.dto;

import java.util.List;

/**
 * A Data Transfer Object (DTO) to represent the complete disaster help response,
 * including disaster details, an icon, and a list of recommended tasks.
 */
public class DisasterHelpResponse {

    private String disasterType;
    private int peopleAffected;
    private String disasterDetails;
    private String recommendedDisaster;
    private String disasterIcon; // URL to an icon/image representing the disaster
    private List<TaskRecommendation> helpTasks;

    public DisasterHelpResponse() {
    }

    public DisasterHelpResponse(String disasterType, int peopleAffected, String disasterDetails,
                                String recommendedDisaster, String disasterIcon, List<TaskRecommendation> helpTasks) {
        this.disasterType = disasterType;
        this.peopleAffected = peopleAffected;
        this.disasterDetails = disasterDetails;
        this.recommendedDisaster = recommendedDisaster;
        this.disasterIcon = disasterIcon;
        this.helpTasks = helpTasks;
    }

    // Getters and Setters
    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public int getPeopleAffected() {
        return peopleAffected;
    }

    public void setPeopleAffected(int peopleAffected) {
        this.peopleAffected = peopleAffected;
    }

    public String getDisasterDetails() {
        return disasterDetails;
    }

    public void setDisasterDetails(String disasterDetails) {
        this.disasterDetails = disasterDetails;
    }

    public String getRecommendedDisaster() {
        return recommendedDisaster;
    }

    public void setRecommendedDisaster(String recommendedDisaster) {
        this.recommendedDisaster = recommendedDisaster;
    }

    public String getDisasterIcon() {
        return disasterIcon;
    }

    public void setDisasterIcon(String disasterIcon) {
        this.disasterIcon = disasterIcon;
    }

    public List<TaskRecommendation> getHelpTasks() {
        return helpTasks;
    }

    public void setHelpTasks(List<TaskRecommendation> helpTasks) {
        this.helpTasks = helpTasks;
    }

    @Override
    public String toString() {
        return "DisasterHelpResponse{" +
               "disasterType='" + disasterType + '\'' +
               ", peopleAffected=" + peopleAffected +
               ", disasterDetails='" + disasterDetails + '\'' +
               ", recommendedDisaster='" + recommendedDisaster + '\'' +
               ", disasterIcon='" + disasterIcon + '\'' +
               ", helpTasks=" + helpTasks +
               '}';
    }
}
