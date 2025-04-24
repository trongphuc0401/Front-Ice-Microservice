package com.frontice.auth_service.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;;
import java.util.UUID;


@Entity
@Table(name = "verification_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Version
    private Long version; // Thêm trường version để hỗ trợ Optimistic Locking

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public UUID getUserId() {
        return id;
    }

    public void setUserId(UUID userId) {
        // Thay vì set vào ID của token, set vào ID của User
        this.user = new User();
        this.user.setId(userId);
    }

    public enum TokenType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }
}
