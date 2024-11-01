package com.kidsworld.kidsping.domain.like.controller;

import com.kidsworld.kidsping.domain.like.dto.request.DislikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.response.LikeBookResponse;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mbti/book")
public class LikeMbtiController {

    private final LikeMbtiService likeMbtiService;

    @GetMapping("/kid/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<LikeBookResponse>>> getBooksLiked(@PathVariable("kidId") Long kidId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), likeMbtiService.getBooksLiked(kidId, pageRequest),
                ExceptionCode.OK.getMessage());
    }

    @PostMapping("/like")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void like(@RequestBody LikeMbtiRequest likeMbtiRequest) {
        likeMbtiService.like(likeMbtiRequest);
    }

    @DeleteMapping("/like")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void likeCancel(@RequestBody LikeCancelMbtiRequest likeCancelMbtiRequest) {
        likeMbtiService.likeCancel(likeCancelMbtiRequest);
    }

    @PostMapping("/dislike")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void dislike(@RequestBody DislikeMbtiRequest disLikeMbtiRequest) {
        likeMbtiService.dislike(disLikeMbtiRequest);
    }

    @DeleteMapping("/dislike")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void dislikeCancel(@RequestBody DislikeCancelMbtiRequest dislikeCancelMbtiRequest) {
        likeMbtiService.dislikeCancel(dislikeCancelMbtiRequest);
    }
}
