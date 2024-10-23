package com.kidsworld.kidsping.domain.like.controller;

import com.kidsworld.kidsping.domain.like.dto.request.DisLikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mbti/book")
public class LikeMbtiController {

    private final LikeMbtiService likeMbtiService;

    @PostMapping("/like")
    public void like(@RequestBody LikeMbtiRequest likeMbtiRequest) {
        likeMbtiService.like(likeMbtiRequest);
    }

    @DeleteMapping("/like")
    public void likeCancel(@RequestBody LikeCancelMbtiRequest likeCancelMbtiRequest) {
        likeMbtiService.likeCancel(likeCancelMbtiRequest);
    }

    @PostMapping("/dislike")
    public void dislike(@RequestBody DisLikeMbtiRequest disLikeMbtiRequest) {
        likeMbtiService.dislike(disLikeMbtiRequest);
    }

    @DeleteMapping("/dislike")
    public void dislikeCancel() {

    }
}
