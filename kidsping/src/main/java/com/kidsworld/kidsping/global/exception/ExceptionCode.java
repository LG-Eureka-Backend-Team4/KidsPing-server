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
    CREATED(201, HttpStatus.CREATED, "successfully created"),

    // 예외 코드 예시
    NOT_FOUND_BOOK(10101, HttpStatus.NOT_FOUND, "존재하지 않는 책입니다."),
    NOT_FOUND_KID_MBTI(10201, HttpStatus.NOT_FOUND, "아이의 MBTI 정보가 없습니다."),
    NOT_FOUND_COMPATIBILITY(10202, HttpStatus.NOT_FOUND, "MBTI 궁합 정보를 찾을 수 없습니다."),


    // Event,
    NOT_FOUND_EVENT(20201, HttpStatus.NOT_FOUND, "존재하지 않는 이벤트입니다."),


    // KID
    NOT_FOUND_KID(40401, HttpStatus.NOT_FOUND, "존재하지 않는 아이입니다."),
    INVALID_REQUEST_FORMAT(40201, HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다."),
    MAX_KID_LIMIT_REACHED(40202, HttpStatus.BAD_REQUEST, "최대 5명의 자녀만 등록할 수 있습니다."),


    //Like
    NOT_FOUND_LIKE_GENRE(50201, HttpStatus.NOT_FOUND, "해당 좋아요/싫어요가 없습니다."),
    ALREADY_LIKED_OR_DISLIKED(50202, HttpStatus.BAD_REQUEST, "이미 좋아요 또는 싫어요를 했습니다."),
    NOT_FOUND_KID_WITH_MBTI(50203, HttpStatus.BAD_REQUEST, "성향을 가진 자녀가 존재하지 않습니다."),
    DUPLICATE_EMPATHY_STATUS(50204, HttpStatus.NOT_FOUND, "자녀의 현재 공감 상태와 요청 공감 상태가 동일합니다."),
    NOT_FOUND_BOOK_WITH_MBTI(50205, HttpStatus.NOT_FOUND, "성향을 가진 도서가 존재하지 않습니다."),
    NOT_FOUND_EMPATHY(50206, HttpStatus.NOT_FOUND, "공감 정보가 없습니다."),


    // 회원
    UNAUTHORIZED_USER(80201, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    NOT_FOUND_USER(80202, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),


    // 500 예외
    INTERNAL_ERROR(90001, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(90002, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),


    NOT_FOUND_GENRE(30201, HttpStatus.NOT_FOUND, "존재하지 않는 장르입니다."),
    NOT_FOUND_GENRE_ANSWER(70201, HttpStatus.NOT_FOUND, "존재하지 않는 장르 응답입니다."),
    NOT_FOUND_MBTI_ANSWER(70202, HttpStatus.NOT_FOUND, "존재하지 않는 성향 응답입니다."),

    //  MBTI (자녀 성향 조회)
    NOT_FOUND_MBTI_INFO(60201, HttpStatus.NOT_FOUND, "MBTI 정보를 찾을 수 없습니다."),

    FILE_COUNT_EXCEEDED(100001, HttpStatus.BAD_REQUEST, "파일 업로드 가능 개수는 10개 이하 입니다."),
    FILE_SIZE_EXCEEDED(100002, HttpStatus.BAD_REQUEST, "업로드 할 수 있는 파일의 최대 크기는 5MB 입니다."),
    FILE_UPLOAD_FAILED(100003, HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),
    FILE_NOT_FOUND(100004, HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다.");

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