package com.frontice.auth_service.util;

public class ResponseUtil {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiStatusCode.SUCCESS, ApiErrorCode.SUCCESS, "Success", data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(ApiStatusCode.CREATED, ApiErrorCode.CREATED, "Created successfully", data);
    }

    public static <T> ApiResponse<T> accountExists() {
        return new ApiResponse<>(ApiStatusCode.METHOD_NOT_ALLOWED, ApiErrorCode.ACCOUNT_EXISTS, "Account exist in system", null);
    }

    public static <T> ApiResponse<T> invalidInput(String message) {
        return new ApiResponse<>(ApiStatusCode.BAD_REQUEST, ApiErrorCode.INVALID_INPUT, message, null);
    }

    public static <T> ApiResponse<T> emailNotVerified() {
        return new ApiResponse<>(ApiStatusCode.BAD_REQUEST, ApiErrorCode.EMAIL_NOT_VERIFIED, "Email not verified", null);
    }

    public static <T> ApiResponse<T> systemError(String message) {
        return new ApiResponse<>(ApiStatusCode.INTERNAL_SERVER_ERROR, ApiErrorCode.SYSTEM_ERROR, message, null);
    }
}
