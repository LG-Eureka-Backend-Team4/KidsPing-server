package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.MBTIQuestionResponse;
import com.kidsworld.kidsping.domain.question.service.MBTIQuestionService;
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
public class MBTIQuestionController {

    private final MBTIQuestionService mbtiQuestionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MBTIQuestionResponse>>> findAllQuestion() {
        List<MBTIQuestionResponse> questionResponses = mbtiQuestionService.findAllQuestion();
        return ApiResponse.ok(ExceptionCode.OK.getCode(), questionResponses, ExceptionCode.OK.getMessage());
    }
}