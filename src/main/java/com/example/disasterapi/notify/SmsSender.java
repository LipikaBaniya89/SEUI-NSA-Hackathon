package com.example.disasterapi.notify;

public interface SmsSender {
    void send(String to, String body) throws Exception;
}