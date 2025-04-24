package com.frontice.auth_service.service.kafka;

import com.frontice.auth_service.kafka.model.EmailVerificationResult;
import com.frontice.auth_service.kafka.model.VerificationEmailEvent;
import com.frontice.auth_service.model.User;
import com.frontice.auth_service.model.VerificationToken;
import com.frontice.auth_service.repository.UserRepository;
import com.frontice.auth_service.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class EmailVerificationService {

    @Autowired
    private KafkaTemplate<String, VerificationEmailEvent> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    private static final String EMAIL_VERIFICATION_TOPIC = "email-verification-topic";

    @Transactional
    public void requestVerificationEmail(UUID userId, String email) {

        String token = generateVerificationToken();

        saveVerificationToken(userId, token);

        VerificationEmailEvent event = new VerificationEmailEvent(userId, email, token);

        kafkaTemplate.send(EMAIL_VERIFICATION_TOPIC, event);

    }

    /**
     * Gửi lại email xác thực
     */
    public boolean resendVerificationEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        // Xóa token cũ nếu có
        tokenRepository.deleteByUser_Id(user.getId());

        // Gửi lại email xác thực
        requestVerificationEmail(user.getId(), email);

        return true;
    }
    /**
     * Xác thực email thông qua token
     */
    public EmailVerificationResult verifyEmail(String token) {
        Optional<VerificationToken> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            return EmailVerificationResult.INVALID;
        }

        VerificationToken verificationToken = tokenOptional.get();

        // Kiểm tra token đã hết hạn chưa
        if (isTokenExpired(verificationToken)) {
            return EmailVerificationResult.EXPIRED;
        }

        // Xác thực người dùng
        Optional<User> userOptional = userRepository.findById(verificationToken.getUserId());

        if (userOptional.isEmpty()) {
            return EmailVerificationResult.INVALID;
        }

        User user = userOptional.get();
        user.setEmailVerified(true);
        userRepository.save(user);

        // Xóa token đã sử dụng
        tokenRepository.delete(verificationToken);

        return EmailVerificationResult.VERIFIED;
    }



    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
    /**
     * Lưu token vào database
     */
    private void saveVerificationToken(UUID id, String token) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("User found: {}", user);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(UUID.randomUUID()); // Đảm bảo ID mới cho token
        verificationToken.setToken(token);
        verificationToken.setUser(user); // Set đúng User vào token
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        log.info("Saving VerificationToken with ID: {} for User: {}", verificationToken.getId(), user.getId());
        tokenRepository.save(verificationToken);
    }

    /**
     * Kiểm tra token đã hết hạn chưa
     */
    private boolean isTokenExpired(VerificationToken token) {
        return LocalDateTime.now().isAfter(token.getExpiryDate());
    }

}
