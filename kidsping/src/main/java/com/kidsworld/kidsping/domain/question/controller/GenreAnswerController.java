package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponse;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre-answers")
@RequiredArgsConstructor
public class GenreAnswerController {

    private final GenreAnswerService genreAnswerService;

    @GetMapping("/kid/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GenreAnswerResponse>>> getGenreAnswerHistory(
            @PathVariable Long kidId) {
        List<GenreAnswerResponse> responses = genreAnswerService.getGenreAnswerHistory(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), responses, "아이의 장르 응답 이력을 성공적으로 조회했습니다.");
    }
}
