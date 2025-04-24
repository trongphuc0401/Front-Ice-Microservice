package com.frontice.auth_service.util;

// ApiErrorCode.java
public class ApiErrorCode {
    // Success codes (positive numbers)
    public static final int SUCCESS = 100; // Thành công
    public static final int CREATED = 101; // Tạo mới thành công
    public static final int UPDATED = 102; // Cập nhật thành công
    public static final int DELETED = 103; // Xóa thành công
    public static final int VERIFIED = 104; // Xác thực thành công

    // Error codes (negative numbers)
    public static final int ACCOUNT_EXISTS = -105; // Tài khoản đã tồn tại
    public static final int INVALID_INPUT = -106; // Dữ liệu đầu vào không hợp lệ
    public static final int EMAIL_NOT_VERIFIED = -107; // Email chưa được xác thực
    public static final int VERIFICATION_EXPIRED = -108; // Mã xác thực đã hết hạn
    public static final int VERIFICATION_FAILED = -109; // Xác thực thất bại
    public static final int EMAIL_SEND_FAILED = -110; // Gửi email thất bại
    public static final int INVALID_CREDENTIALS = -111; // Thông tin đăng nhập không hợp lệ
    public static final int USER_NOT_FOUND = -112; // Không tìm thấy người dùng
    public static final int SYSTEM_ERROR = -500; // Lỗi hệ thống
}