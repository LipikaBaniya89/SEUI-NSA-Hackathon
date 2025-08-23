package com.example.disasterapi.model;

import lombok.Data;

@Data
public class Content {
    private String role;
    private Part[] parts;
}
