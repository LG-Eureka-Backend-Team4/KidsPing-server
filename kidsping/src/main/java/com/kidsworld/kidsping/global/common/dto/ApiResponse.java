package com.kidsworld.kidsping.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    private Integer code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(String message, Integer code) {
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    private ApiResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String redirectUrl, Integer code, T data, String message) {
        return ResponseEntity.created(URI.create(redirectUrl)).body(new ApiResponse<>(code, data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(Integer code, T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(code, data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(Integer code, String message) {
        return ResponseEntity.status(400).body(new ApiResponse<>(code, null, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(Integer code, String message) {
        return ResponseEntity.status(403).body(new ApiResponse<>(code, null, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> unAuthorized(Integer code, String message) {
        return ResponseEntity.status(401).body(new ApiResponse<>(code, null, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(Integer code, String message) {
        return ResponseEntity.status(404).body(new ApiResponse<>(code, null, message));
    }

}