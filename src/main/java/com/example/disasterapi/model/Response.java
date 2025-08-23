package com.example.disasterapi.model;

import lombok.Data;

@Data
public class Response {
    private Candidate[] candidates;
    private ApiError error;
}

//helper classes


@Data
class Candidate{
    private Content content;

    public Content getContent(){
        return content;
    }
}

@Data
class ApiError{
    private int code;
    private String message;

    public String getMessage(){
        return message;
    }
}
