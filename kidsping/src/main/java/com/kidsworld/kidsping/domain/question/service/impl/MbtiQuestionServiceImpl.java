package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiQuestionResponse;
import com.kidsworld.kidsping.domain.question.entity.MbtiQuestion;
import com.kidsworld.kidsping.domain.question.repository.MbtiQuestionRepository;
import com.kidsworld.kidsping.domain.question.service.MbtiQuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbtiQuestionServiceImpl implements MbtiQuestionService {

    private final MbtiQuestionRepository mbtiQuestionRepository;

    @Override
    public List<MbtiQuestionResponse> findAllQuestion() {
        return getMbtiQuestionResponses();
    }

    private List<MbtiQuestionResponse> getMbtiQuestionResponses() {
        List<MbtiQuestion> MbtiQuestions = mbtiQuestionRepository.findAll();
        return MbtiQuestions.stream()
                .map(MbtiQuestionResponse::from)
                .toList();
    }
}