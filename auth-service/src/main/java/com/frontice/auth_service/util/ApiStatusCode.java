package com.frontice.auth_service.util;

public class ApiStatusCode {
    // Success codes (200-299)
    public static final int SUCCESS = 200; // Thành công
    public static final int CREATED = 201; // Tạo mới thành công
    public static final int ACCEPTED = 202; // Đã chấp nhận yêu cầu

    // Client error codes (400-499)
    public static final int BAD_REQUEST = 400; // Yêu cầu không hợp lệ
    public static final int UNAUTHORIZED = 401; // Không có quyền truy cập
    public static final int FORBIDDEN = 403; // Bị cấm truy cập
    public static final int NOT_FOUND = 404; // Không tìm thấy
    public static final int METHOD_NOT_ALLOWED = 405; // Phương thức không được phép
    public static final int CONFLICT = 409; // Xung đột dữ liệu

    // Server error codes (500-599)
    public static final int INTERNAL_SERVER_ERROR = 500; // Lỗi server
    public static final int NOT_IMPLEMENTED = 501; // Chưa được thực hiện
    public static final int SERVICE_UNAVAILABLE = 503; // Dịch vụ không khả dụng
}
