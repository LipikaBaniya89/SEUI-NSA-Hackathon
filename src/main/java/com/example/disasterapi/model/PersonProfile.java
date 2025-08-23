package com.example.disasterapi.model;

import lombok.Data;

@Data
public class PersonProfile {


    public String getHobbies(){
        return "Video games";
    }
    public int getAge(){
        return 25;
    }
    public String getOccupation(){
        return "Player";
    }

}
