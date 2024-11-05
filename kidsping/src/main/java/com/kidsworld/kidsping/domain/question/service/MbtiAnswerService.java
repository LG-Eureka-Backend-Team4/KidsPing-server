package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiAnswerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MbtiAnswerService {

    void deleteExpiredMbtiAnswer();

    MbtiAnswerResponse getMbtiAnswer(Long mbtiAnswerId);

    Page<MbtiAnswerResponse> getMbtiAnswers(Long kidId, Pageable pageable);

    void deleteMbtiAnswer(Long mbtiAnswerId);
}
