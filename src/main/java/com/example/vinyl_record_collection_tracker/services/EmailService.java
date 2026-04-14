package com.example.vinyl_record_collection_tracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vinyltracker.noreply@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Vinyl Tracker - Password Reset Request");
            message.setText(
                    "Hi,\n\n" +
                            "We received a request to reset your Vinyl Tracker password.\n\n" +
                            "Click the link below to reset your password. This link expires in 30 minutes:\n\n" +
                            resetLink + "\n\n" +
                            "If you did not request a password reset, you can safely ignore this email.\n\n"
            );

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
