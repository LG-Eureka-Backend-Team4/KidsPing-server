package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponseDto;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<GenreAnswerResponseDto>> getGenreAnswer(@PathVariable Long id) {
        GenreAnswerResponseDto response = genreAnswerService.getGenreAnswer(id);
        return ApiResponse.ok(200, response, "장르 응답을 성공적으로 조회했습니다.");
    }

    @GetMapping("/kid/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GenreAnswerResponseDto>>> getGenreAnswersByKidId(
            @PathVariable Long kidId) {
        List<GenreAnswerResponseDto> responses = genreAnswerService.getGenreAnswersByKidId(kidId);
        return ApiResponse.ok(200, responses, "아이의 장르 응답 목록을 성공적으로 조회했습니다.");
    }
}
