package com.example.disasterapi.dto;

public class PersonInfo {
    private String name;
    private String skill;
    private String location;

    public PersonInfo() {}

    public PersonInfo(String name, String skill, String location) {
        this.name = name;
        this.skill = skill;
        this.location = location;
    }

    public String getName() { return name; }
    public String getSkill() { return skill; }
    public String getLocation() { return location; }

    public void setName(String name) { this.name = name; }
    public void setSkill(String skill) { this.skill = skill; }
    public void setLocation(String location) { this.location = location; }
}
