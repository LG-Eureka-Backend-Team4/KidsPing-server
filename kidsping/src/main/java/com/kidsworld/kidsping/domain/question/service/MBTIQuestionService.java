package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.MBTIQuestionResponse;
import java.util.List;

public interface MBTIQuestionService {
    List<MBTIQuestionResponse> findAllQuestion();
}
