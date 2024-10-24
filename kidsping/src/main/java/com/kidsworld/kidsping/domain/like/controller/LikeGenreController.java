package com.kidsworld.kidsping.domain.like.controller;

import com.kidsworld.kidsping.domain.like.dto.request.LikeGenreRequest;
import com.kidsworld.kidsping.domain.like.service.LikeGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
public class LikeGenreController {

    @Autowired
    private LikeGenreService likeGenreService;

    // 좋아요 처리
    @PostMapping("/like")
    public ResponseEntity<Void> likeGenre(@RequestBody LikeGenreRequest request) {
        likeGenreService.like(request.getKidId(), request.getBookId());
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소 처리
    @DeleteMapping("/like")
    public ResponseEntity<Void> likeCancel(@RequestBody LikeGenreRequest request) {
        likeGenreService.likeCancel(request.getKidId(), request.getBookId());
        return ResponseEntity.ok().build();
    }

    // 싫어요 처리
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeGenre(@RequestBody LikeGenreRequest request) {
        likeGenreService.dislike(request.getKidId(), request.getBookId());
        return ResponseEntity.ok().build();
    }

    // 싫어요 취소 처리
    @DeleteMapping("/dislike")
    public ResponseEntity<Void> dislikeCancel(@RequestBody LikeGenreRequest request) {
        likeGenreService.dislikeCancel(request.getKidId(), request.getBookId());
        return ResponseEntity.ok().build();
    }
}
