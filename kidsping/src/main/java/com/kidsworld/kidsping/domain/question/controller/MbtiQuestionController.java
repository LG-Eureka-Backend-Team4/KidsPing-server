package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiQuestionResponse;
import com.kidsworld.kidsping.domain.question.service.MbtiQuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions/kid/mbti")
public class MbtiQuestionController {

    private final MbtiQuestionService mbtiQuestionService;

    @GetMapping
    public List<MbtiQuestionResponse> findAllQuestion() {
        return mbtiQuestionService.findAllQuestion();
    }
}