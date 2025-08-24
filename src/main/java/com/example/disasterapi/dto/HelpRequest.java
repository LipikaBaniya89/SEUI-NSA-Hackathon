package com.example.disasterapi.dto;

/**
 * A Data Transfer Object (DTO) to represent the incoming request body for the
 * disaster help API endpoint. This encapsulates all the request parameters
 * into a single object for cleaner code.
 */
public class HelpRequest {

    private String name;
    private String skill;
    private String location;
    private int radius;

    // Default constructor is required for Jackson to deserialize the JSON
    public HelpRequest() {}

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
