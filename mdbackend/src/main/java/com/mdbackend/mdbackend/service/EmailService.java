package com.mdbackend.mdbackend.service;

public interface EmailService {
    public boolean sendEmail(String to, String subject, String text);
}
