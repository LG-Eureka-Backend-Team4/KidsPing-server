package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiAnswerResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MbtiAnswerService {

    /*
     * isDeleted 값이 true 이면서 날짜가 현재 기준으로 한달이 지난 id 값을 조회하는 메서드
     * */
    List<Long> findExpiredMbtiAnswerIds();

    void deleteExpiredMbtiAnswer(List<Long> expiredMbtiAnswerIds);

    MbtiAnswerResponse getMbtiAnswer(Long mbtiAnswerId);

    Page<MbtiAnswerResponse> getMbtiAnswers(Long kidId, Pageable pageable);

    void deleteMbtiAnswer(Long mbtiAnswerId);
}
