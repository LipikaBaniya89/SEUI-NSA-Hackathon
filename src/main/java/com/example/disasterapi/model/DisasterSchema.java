package com.example.disasterapi.model;

import lombok.Data;



@Data
public class DisasterSchema extends Schema {

    private HelpAction[] actions;
    @Data
    public static class HelpAction {
        private String task;
    }
}
