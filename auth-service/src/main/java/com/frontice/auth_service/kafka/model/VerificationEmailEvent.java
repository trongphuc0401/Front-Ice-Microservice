package com.frontice.auth_service.kafka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

// VerificationEmailEvent.java
@Getter
@Setter
public class VerificationEmailEvent {
    private UUID id;
    private String email;
    private String token;

    public VerificationEmailEvent(UUID id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }

    // Getters and setters
}
