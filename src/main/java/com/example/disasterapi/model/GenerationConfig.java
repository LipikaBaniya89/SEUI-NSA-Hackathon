package com.example.disasterapi.model;

import lombok.Data;

@Data
public class GenerationConfig {
    private String responseMimeType;
    private Schema responseSchema;
}
