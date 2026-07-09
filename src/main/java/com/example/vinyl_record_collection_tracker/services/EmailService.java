package com.example.vinyl_record_collection_tracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {
    private final WebClient webClient;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.address}")
    private String fromAddress;

    public EmailService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.resend.com")
                .build();
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        String htmlBody =
                "<p>Hi,</p>" +
                        "<p>We received a request to reset your Vinyl Tracker password.</p>" +
                        "<p><a href=\"" + resetLink + "\">Click here to reset your password</a>. This link expires in 30 minutes.</p>" +
                        "<p>If you did not request a password reset, you can safely ignore this email.</p>";

        Map<String, Object> payload = Map.of(
                "from", fromAddress,
                "to", toEmail,
                "subject", "Vinyl Tracker - Password Reset Request",
                "html", htmlBody
        );

        try {
            webClient.post()
                    .uri("/emails")
                    .header("Authorization", "Bearer " + resendApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}