package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiQuestionResponse;
import java.util.List;

public interface MbtiQuestionService {
    List<MbtiQuestionResponse> findAllQuestion();
}
