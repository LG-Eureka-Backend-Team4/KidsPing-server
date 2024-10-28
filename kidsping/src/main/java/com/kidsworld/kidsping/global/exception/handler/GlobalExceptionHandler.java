package com.kidsworld.kidsping.global.exception.handler;

import com.kidsworld.kidsping.domain.event.exception.EventNotFoundException;
import com.kidsworld.kidsping.domain.kid.exception.MaxKidLimitReachedException;
import com.kidsworld.kidsping.domain.kid.exception.NotFoundKidException;
import com.kidsworld.kidsping.domain.mbti.exception.NotFoundMbtiInfoException;
import com.kidsworld.kidsping.domain.user.exception.UnauthorizedUserException;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.GlobalException;
import com.kidsworld.kidsping.global.exception.custom.NotFoundException;
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

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ApiResponse> eventNotFoundExceptionHandler(GlobalException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }

    @ExceptionHandler(NotFoundMbtiInfoException.class)
    public ResponseEntity<ApiResponse> NotFoundMbtiInfoException(NotFoundMbtiInfoException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }


    @ExceptionHandler(NotFoundKidException.class)
    public ResponseEntity<ApiResponse> notFoundKidExceptionHandler(NotFoundKidException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }

    @ExceptionHandler(MaxKidLimitReachedException.class)
    public ResponseEntity<ApiResponse> maxKidLimitReachedExceptionHandler(MaxKidLimitReachedException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ApiResponse> unauthorizedUserExceptionHandler(UnauthorizedUserException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity
                .status(e.getErrorExceptionCode().getHttpStatus())
                .body(new ApiResponse(
                        e.getErrorExceptionCode().getMessage(),
                        e.getErrorExceptionCode().getCode())
                );
    }
}