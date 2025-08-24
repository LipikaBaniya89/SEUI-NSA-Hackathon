package com.example.disasterapi.dto;

import java.util.List;

/**
 * A Data Transfer Object (DTO) to represent the complete disaster help response,
 * including disaster details and a list of recommended tasks.
 */
public class DisasterHelpResponse {

    private String disasterType;
    private int peopleAffected;
    private String disasterDetails;
    private String recommendedDisaster;
    private List<TaskRecommendation> helpTasks;

    public DisasterHelpResponse() {
    }

    public DisasterHelpResponse(String disasterType, int peopleAffected, String disasterDetails, String recommendedDisaster, List<TaskRecommendation> helpTasks) {
        this.disasterType = disasterType;
        this.peopleAffected = peopleAffected;
        this.disasterDetails = disasterDetails;
        this.recommendedDisaster = recommendedDisaster;
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
               ", helpTasks=" + helpTasks +
               '}';
    }
}
