package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.question.repository.MbtiAnswerRepository;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbtiAnswerServiceImple implements MbtiAnswerService {

    private final MbtiAnswerRepository mbtiAnswerRepository;

    /*
     * isDeleted 값이 true 이면서 날짜가 현재 기준으로 한달이 지난 id 값을 조회하는 메서드
     * */
    @Override
    public List<Long> findExpiredMbtiAnswerIds() {
        return mbtiAnswerRepository.findExpiredMbtiAnswerIds(LocalDateTime.now());
    }

    @Override
    public void deleteExpiredMbtiAnswer(List<Long> expiredMbtiAnswerIds) {
        mbtiAnswerRepository.deleteExpiredMbtiAnswer(expiredMbtiAnswerIds);
    }
}
