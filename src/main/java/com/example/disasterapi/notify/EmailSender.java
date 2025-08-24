package com.example.disasterapi.notify;

public interface EmailSender {
    void send(String to, String subject, String body) throws Exception;
}