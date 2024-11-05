package com.kidsworld.kidsping.domain.genre.controller;

import com.kidsworld.kidsping.domain.genre.dto.request.GenreScoreRequest;
import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genre-score")
public class GenreScoreController {

    private final GenreScoreService genreScoreService;

    @PostMapping("/addScore")
    public ResponseEntity<String> addGenreScore(@RequestBody GenreScoreRequest request) {
        genreScoreService.addGenreScore(request.getKidId(), request.getGenreIds());
        return ResponseEntity.ok("장르 점수 업데이트 성공");
    }

    @GetMapping("/kid/{kidId}/top")
    public ResponseEntity<ApiResponse<TopGenreResponse>> getTopGenre(@PathVariable Long kidId) {
        TopGenreResponse response = genreScoreService.getTopGenre(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "아이의 최고 점수 장르를 성공적으로 조회했습니다.");
    }
}