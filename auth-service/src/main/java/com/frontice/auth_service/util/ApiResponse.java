package com.frontice.auth_service.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private int status;
    private int code;
    private String messageEng;
    private T data;
    private String timestamp;

    public ApiResponse(int status, int code, String messageEng, T data) {
        this.status = status;
        this.code = code;
        this.messageEng = messageEng;
        this.data = data;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
}
