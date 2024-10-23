package com.kidsworld.kidsping.domain.genre.controller;

import com.kidsworld.kidsping.domain.genre.dto.request.GenreScoreRequest;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}