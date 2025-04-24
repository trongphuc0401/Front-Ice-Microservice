package com.frontice.auth_service.service.kafka;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// EmailService.java
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String toEmail, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Email Verification");

            String emailContent =
                    "<html><body>" +
                            "<h2>Xác thực tài khoản của bạn</h2>" +
                            "<p>Cảm ơn bạn đã đăng ký. Vui lòng nhấp vào liên kết dưới đây để xác thực email của bạn:</p>" +
                            "<a href='" + verificationLink + "'>Xác thực email</a>" +
                            "<p>Liên kết này sẽ hết hạn sau 24 giờ.</p>" +
                            "</body></html>";

            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
