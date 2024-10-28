package com.kidsworld.kidsping.domain.question.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.question.dto.response.MbtiAnswerResponse;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import com.kidsworld.kidsping.domain.question.repository.MbtiAnswerRepository;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.custom.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class MbtiAnswerServiceImpleTest {

    @Autowired
    private MbtiAnswerService mbtiAnswerService;

    @Autowired
    private MbtiAnswerRepository mbtiAnswerRepository;

    @Autowired
    private KidRepository kidRepository;

    @DisplayName("설문 조사 id값을 받아 설문 응답 dto를 조회한다.")
    @Test
    void getMbtiAnswer() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        MbtiAnswer mbtiAnswer = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);

        // when
        kidRepository.save(kid);
        mbtiAnswerRepository.save(mbtiAnswer);
        MbtiAnswerResponse mbtiAnswerResponse = mbtiAnswerService.getMbtiAnswer(mbtiAnswer.getId());

        // then
        assertThat(mbtiAnswerResponse.getId()).isNotNull();
        assertThat(mbtiAnswerResponse)
                .extracting("kidId", "eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore")
                .containsExactly(
                        kid.getId(), 3, 2, 4, 5, 6, 7, 8, 1
                );
    }

    @DisplayName("자녀가 응답한 설문 결과 목록을 조회한다.")
    @Test
    void getMbtiAnswers() {
        // given
        Kid kid1 = createKid(Gender.MALE, "이름1", LocalDate.now());
        Kid kid2 = createKid(Gender.FEMALE, "이름2", LocalDate.now());
        MbtiAnswer mbtiAnswer1 = createMbtiAnswer(kid1, 3, 2, 4, 5, 6, 7, 8, 1);
        MbtiAnswer mbtiAnswer2 = createMbtiAnswer(kid1, 13, 12, 14, 15, 16, 17, 18, 11);
        MbtiAnswer mbtiAnswer3 = createMbtiAnswer(kid2, 3, 2, 4, 5, 6, 7, 8, 1);
        Pageable pageable = PageRequest.of(0, 2);

        // when
        kidRepository.saveAll(List.of(kid1, kid2));
        mbtiAnswerRepository.saveAll(List.of(mbtiAnswer1, mbtiAnswer2, mbtiAnswer3));
        Page<MbtiAnswerResponse> mbtiAnswerResponses = mbtiAnswerService.getMbtiAnswers(kid1.getId(), pageable);

        // then
        assertThat(mbtiAnswerResponses.getContent())
                .hasSize(2)
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore")
                .containsExactlyInAnyOrder(
                        tuple(3, 2, 4, 5, 6, 7, 8, 1),
                        tuple(13, 12, 14, 15, 16, 17, 18, 11)
                );
    }

    @DisplayName("삭제한 설문 결과는 조회하지 못한다.")
    @Test
    void deleteMbtiAnswer() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        MbtiAnswer mbtiAnswer = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);

        // when
        kidRepository.save(kid);
        MbtiAnswer savedMbtiAnswer = mbtiAnswerRepository.save(mbtiAnswer);
        savedMbtiAnswer.delete();

        // then
        assertThatThrownBy(() -> mbtiAnswerService.getMbtiAnswer(mbtiAnswer.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_MBTI_ANSWER)
                .hasMessage(ExceptionCode.NOT_FOUND_MBTI_ANSWER.getMessage());
    }

    private static Kid createKid(Gender gender, String name, LocalDate birth) {
        return Kid.builder()
                .gender(gender)
                .name(name)
                .birth(birth)
                .build();
    }

    private static MbtiAnswer createMbtiAnswer(Kid kid, int e, int i, int s, int n, int t, int f, int p, int j) {
        return MbtiAnswer
                .builder()
                .kid(kid)
                .eScore(e)
                .iScore(i)
                .sScore(s)
                .nScore(n)
                .tScore(t)
                .fScore(f)
                .pScore(p)
                .jScore(j)
                .isDeleted(false)
                .build();
    }
}