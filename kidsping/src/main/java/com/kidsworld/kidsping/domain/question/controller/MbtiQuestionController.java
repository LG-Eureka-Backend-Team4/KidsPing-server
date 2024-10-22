package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiQuestionResponse;
import com.kidsworld.kidsping.domain.question.service.MbtiQuestionService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions/kid/mbti")
public class MbtiQuestionController {

    private final MbtiQuestionService mbtiQuestionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MbtiQuestionResponse>>> findAllQuestion() {
        List<MbtiQuestionResponse> questionResponses = mbtiQuestionService.findAllQuestion();
        return ApiResponse.ok(ExceptionCode.OK.getCode(), questionResponses, ExceptionCode.OK.getMessage());
    }
}