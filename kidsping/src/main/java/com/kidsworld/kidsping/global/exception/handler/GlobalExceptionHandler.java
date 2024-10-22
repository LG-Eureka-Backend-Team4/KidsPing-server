package com.kidsworld.kidsping.global.exception.handler;

import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse> globalExceptionHandler(GlobalException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> userNotFoundExceptionHandler(GlobalException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }
}