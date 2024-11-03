package com.kidsworld.kidsping.domain.like.controller;

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
    public ResponseEntity<Void> likeGenre(@RequestParam Long kidId, @RequestParam Long bookId) {
        likeGenreService.like(kidId, bookId);
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소 처리
    @DeleteMapping("/like")
    public ResponseEntity<Void> likeCancel(@RequestParam Long kidId, @RequestParam Long bookId) {
        likeGenreService.likeCancel(kidId, bookId);
        return ResponseEntity.ok().build();
    }

    // 싫어요 처리
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeGenre(@RequestParam Long kidId, @RequestParam Long bookId) {
        likeGenreService.dislike(kidId, bookId);
        return ResponseEntity.ok().build();
    }

    // 싫어요 취소 처리
    @DeleteMapping("/dislike")
    public ResponseEntity<Void> dislikeCancel(@RequestParam Long kidId, @RequestParam Long bookId) {
        likeGenreService.dislikeCancel(kidId, bookId);
        return ResponseEntity.ok().build();
    }
}
