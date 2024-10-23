package com.kidsworld.kidsping.global.exception;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 정상 응답 코드
    OK(200, HttpStatus.OK, "success"),

    // 예외 코드 예시
    NOT_FOUND_BOOK(10101, HttpStatus.NOT_FOUND, "존재하지 않는 책입니다."),

    // KID
    NOT_FOUND_KID(40401, HttpStatus.NOT_FOUND, "존재하지 않는 아이입니다."),
    MAX_KID_LIMIT_REACHED(40202, HttpStatus.BAD_REQUEST,"최대 5명의 자녀만 등록할 수 있습니다."),


    // 회원
    UNAUTHORIZED_USER(80201, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    NOT_FOUND_USER(80202, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // 500 예외
    INTERNAL_ERROR(90001, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(90002, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),


    NOT_FOUND_GENRE(30201, HttpStatus.NOT_FOUND, "존재하지 않는 장르입니다."),
    NOT_FOUND_GENRE_ANSWER(70201, HttpStatus.NOT_FOUND, "존재하지 않는 장르 응답입니다."),


    //  MBTI (자녀 성향 조회)
    NOT_FOUND_MBTI_INFO(60201, HttpStatus.NOT_FOUND, "MBTI 정보를 찾을 수 없습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}