package com.frontice.auth_service.service;


import com.frontice.auth_service.dto.request.RegistrationRequest;
import com.frontice.auth_service.dto.response.RegistrationResponse;
import com.frontice.auth_service.exception.UserAlreadyExistsException;
import com.frontice.auth_service.model.Role;
import com.frontice.auth_service.model.RoleName;
import com.frontice.auth_service.model.User;
import com.frontice.auth_service.repository.RoleRepository;
import com.frontice.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        // Check if user already exists
        if (Boolean.TRUE.equals(userRepository.existsByEmail(registrationRequest.getEmail()))) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        if (!registrationRequest.getPasswordConfirmation().contains(registrationRequest.getPassword())) {
            throw new UserAlreadyExistsException("Password not match");
        }

        // Create new user
        User user = User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phone(registrationRequest.getPhone())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .company(registrationRequest.getCompany())
                .enabled(true)
                .roles(new HashSet<>())
                .build();

        // Add default ROLE_USER
        Role userRole = roleRepository.findByName(RoleName.ROLE_CHALLENGER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        user.getRoles().add(userRole);

        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        // Return response
        return RegistrationResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .phone(savedUser.getPhone())
                .company(savedUser.getCompany())
                .role(savedUser.getRoles().toString())
                .build();
    }



}
