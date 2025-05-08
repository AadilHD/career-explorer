package com.rbc.explorer.careerexplorer;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmation(String toEmail, String jobTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Application Received - " + jobTitle);
        message.setText("Thank you for applying to \"" + jobTitle + "\".\nWe’ll review your application soon.");
        message.setFrom("ahdhalla@gmail.com"); // Must match the username above

        mailSender.send(message);
    }
}