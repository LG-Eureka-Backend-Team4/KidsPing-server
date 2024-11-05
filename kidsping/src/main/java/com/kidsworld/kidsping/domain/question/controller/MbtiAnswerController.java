package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiAnswerResponse;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers/mbti")
public class MbtiAnswerController {

    private final MbtiAnswerService mbtiAnswerService;

    @GetMapping("/{answerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<MbtiAnswerResponse>> getMbtiAnswer(@PathVariable("answerId") Long mbtiAnswerId) {
        MbtiAnswerResponse mbtiAnswer = mbtiAnswerService.getMbtiAnswer(mbtiAnswerId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), mbtiAnswer, "아이의 성향 응답 이력을 성공적으로 조회했습니다.");
    }

    @GetMapping("/kid/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<MbtiAnswerResponse>>> getMbtiAnswers(@PathVariable("kidId") Long kidId,
                                                                                Pageable pageable) {
        Page<MbtiAnswerResponse> mbtiAnswerResponses = mbtiAnswerService.getMbtiAnswers(kidId, pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), mbtiAnswerResponses, "아이의 성향 응답 이력 목록을 성공적으로 조회했습니다.");
    }

    @DeleteMapping("/{answerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteMbtiAsnwer(@PathVariable("answerId") Long mbtiAnswerId) {
        mbtiAnswerService.deleteMbtiAnswer(mbtiAnswerId);
    }
}
