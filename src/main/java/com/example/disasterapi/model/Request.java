package com.example.disasterapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class Request {
    private Content[] contents;
    private Content systemInstruction;
    private GenerationConfig generationConfig;
}
