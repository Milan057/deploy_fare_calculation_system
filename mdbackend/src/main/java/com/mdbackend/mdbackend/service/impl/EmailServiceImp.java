package com.mdbackend.mdbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.service.EmailService;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailServiceImp implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String fromEmail;

    @Override
    public boolean sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(fromEmail);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false; 
        }
    }

    

}
