package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.question.dto.response.MBTIQuestionResponse;
import com.kidsworld.kidsping.domain.question.entity.MBTIQuestion;
import com.kidsworld.kidsping.domain.question.repository.MBTIQuestionRepository;
import com.kidsworld.kidsping.domain.question.service.MBTIQuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MBTIQuestionServiceImpl implements MBTIQuestionService {

    private final MBTIQuestionRepository mbtiQuestionRepository;

    @Override
    public List<MBTIQuestionResponse> findAllQuestion() {
        return getMbtiQuestionResponses();
    }

    private List<MBTIQuestionResponse> getMbtiQuestionResponses() {
        List<MBTIQuestion> MBTIQuestions = mbtiQuestionRepository.findAll();
        return MBTIQuestions.stream()
                .map(MBTIQuestionResponse::from)
                .toList();
    }
}