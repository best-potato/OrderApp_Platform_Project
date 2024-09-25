package com.sparta.orderapp.dto.common;


import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String message;
    private final Integer statusCode;

    private ApiResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> createSuccess(String message, Integer statusCode, T data) {
        return new ApiResponse<>(message, statusCode);
    }

    /**
     * 에러 응답 생성
     */
    public static <T> ApiResponse<T> createError(String message, Integer statusCode) {
        return new ApiResponse<>(message, statusCode);
    }

}
