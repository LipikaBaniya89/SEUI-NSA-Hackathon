package com.example.disasterapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {
    private String prompt;
    private String systemInstructions;
    private Schema schema;
}
