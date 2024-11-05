package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiAnswerResponse;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import com.kidsworld.kidsping.domain.question.repository.MbtiAnswerRepository;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.custom.NotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MbtiAnswerServiceImple implements MbtiAnswerService {

    private final MbtiAnswerRepository mbtiAnswerRepository;

    @Transactional(readOnly = true)
    @Override
    public MbtiAnswerResponse getMbtiAnswer(Long mbtiAnswerId) {
        MbtiAnswer mbtiAnswer = findMbtiAnswerBy(mbtiAnswerId);
        return MbtiAnswerResponse.from(mbtiAnswer);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MbtiAnswerResponse> getMbtiAnswers(Long kidId, Pageable pageable) {
        Page<MbtiAnswer> mbtiAnswers = mbtiAnswerRepository.findMbtiAnswersBy(kidId, pageable);
        return mbtiAnswers.map(MbtiAnswerResponse::from);
    }

    @Transactional
    @Override
    public void deleteMbtiAnswer(Long mbtiAnswerId) {
        MbtiAnswer mbtiAnswer = findMbtiAnswerBy(mbtiAnswerId);
        mbtiAnswer.delete();
    }

    private MbtiAnswer findMbtiAnswerBy(Long mbtiAnswerId) {
        return mbtiAnswerRepository.findMbtiAnswerBy(mbtiAnswerId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MBTI_ANSWER));
    }

    @Override
    public void deleteExpiredMbtiAnswer() {
        mbtiAnswerRepository.deleteExpiredMbtiAnswer(LocalDateTime.now());
    }
}