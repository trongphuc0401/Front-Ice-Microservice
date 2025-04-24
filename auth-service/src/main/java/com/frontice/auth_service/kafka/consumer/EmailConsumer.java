package com.frontice.auth_service.kafka.consumer;

import com.frontice.auth_service.kafka.model.VerificationEmailEvent;
import com.frontice.auth_service.service.kafka.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "email-verification-topic", groupId = "email-service-group")
    public void consume(VerificationEmailEvent event) {
        try {
            // Xử lý và gửi email
            String verificationLink = generateVerificationLink(event.getToken());
            emailService.sendVerificationEmail(event.getEmail(), verificationLink);
        } catch (Exception e) {
            // Xử lý lỗi khi gửi email
            System.err.println("Error sending verification email: " + e.getMessage());
            // Có thể thêm logic retry hoặc ghi log
        }
    }

    private String generateVerificationLink(String token) {
        return "http://your-domain.com/verify-email?token=" + token;
    }
}