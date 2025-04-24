package com.frontice.auth_service.controller;
import com.frontice.auth_service.dto.request.RegistrationRequest;
import com.frontice.auth_service.dto.response.RegistrationResponse;
import com.frontice.auth_service.kafka.model.EmailVerificationResult;
import com.frontice.auth_service.service.UserService;
import com.frontice.auth_service.service.kafka.EmailVerificationService;
import com.frontice.auth_service.util.ApiErrorCode;
import com.frontice.auth_service.util.ApiResponse;
import com.frontice.auth_service.util.ApiStatusCode;
import com.frontice.auth_service.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
     UserService userService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@Valid @RequestBody RegistrationRequest request) {
        try {

            // Đăng ký người dùng
            RegistrationResponse registrationResponse = userService.registerUser(request);

            // Gửi yêu cầu gửi email thông qua Kafka

            emailVerificationService.requestVerificationEmail(registrationResponse.getId(), request.getEmail());

            return ResponseEntity.status(ApiStatusCode.CREATED)
                    .body(ResponseUtil.created(registrationResponse));
        } catch (Exception e) {
            return ResponseEntity.status(ApiStatusCode.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.systemError("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<ApiResponse<Void>> sendVerificationEmail(@RequestParam String email) {
        try {
            boolean sent = emailVerificationService.resendVerificationEmail(email);

            if (!sent) {
                return ResponseEntity.status(ApiStatusCode.BAD_REQUEST)
                        .body(ResponseUtil.invalidInput("Email not found in system"));
            }

            return ResponseEntity.ok(new ApiResponse<>(
                    ApiStatusCode.SUCCESS,
                    ApiErrorCode.SUCCESS,
                    "Verification email sent successfully",
                    null));
        } catch (Exception e) {
            return ResponseEntity.status(ApiStatusCode.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.systemError("Failed to send verification email: " + e.getMessage()));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        try {
            EmailVerificationResult result = emailVerificationService.verifyEmail(token);

            switch (result) {
                case VERIFIED:
                    return ResponseEntity.ok(new ApiResponse<>(
                            ApiStatusCode.SUCCESS,
                            ApiErrorCode.VERIFIED,
                            "Email verified successfully",
                            null));
                case EXPIRED:
                    return ResponseEntity.status(ApiStatusCode.BAD_REQUEST)
                            .body(new ApiResponse<>(
                                    ApiStatusCode.BAD_REQUEST,
                                    ApiErrorCode.VERIFICATION_EXPIRED,
                                    "Verification token expired",
                                    null));
                case INVALID:
                default:
                    return ResponseEntity.status(ApiStatusCode.BAD_REQUEST)
                            .body(new ApiResponse<>(
                                    ApiStatusCode.BAD_REQUEST,
                                    ApiErrorCode.VERIFICATION_FAILED,
                                    "Invalid verification token",
                                    null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(ApiStatusCode.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.systemError("Verification failed: " + e.getMessage()));
        }
    }
}
