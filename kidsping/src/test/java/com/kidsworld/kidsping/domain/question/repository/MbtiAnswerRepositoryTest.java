package com.kidsworld.kidsping.domain.question.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class MbtiAnswerRepositoryTest {

    @Autowired
    private MbtiAnswerRepository mbtiAnswerRepository;

    @Autowired
    private KidRepository kidRepository;

    @DisplayName("원하는 자녀 성향을 가진 MBTI 응답 목록을 조회한다.")
    @Test
    void findMbtiAnswersByKidId_ReturnsAnswersForSpecificKid() {
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
        Page<MbtiAnswer> mbtiAnswersPage = mbtiAnswerRepository.findMbtiAnswersBy(kid1.getId(), pageable);

        // then
        assertThat(mbtiAnswersPage.getContent())
                .hasSize(2)
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore")
                .containsExactlyInAnyOrder(
                        tuple(3, 2, 4, 5, 6, 7, 8, 1),
                        tuple(13, 12, 14, 15, 16, 17, 18, 11)
                );
    }

    @DisplayName("삭제된 응답 목록은 조회하지 않는다.")
    @Test
    void findMbtiAnswersByKidId_DoesNotReturnDeletedAnswers() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        MbtiAnswer mbtiAnswer1 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);
        MbtiAnswer mbtiAnswer2 = createMbtiAnswer(kid, 13, 12, 14, 15, 16, 17, 18, 11);
        Pageable pageable = PageRequest.of(0, 2);

        // when
        kidRepository.save(kid);
        mbtiAnswerRepository.saveAll(List.of(mbtiAnswer1, mbtiAnswer2));
        mbtiAnswer1.delete();
        mbtiAnswer2.delete();
        Page<MbtiAnswer> mbtiAnswersPage = mbtiAnswerRepository.findMbtiAnswersBy(kid.getId(), pageable);

        // then
        assertThat(mbtiAnswersPage.getContent()).hasSize(0);
    }

    @DisplayName("한 달 이상 지난 삭제된 MBTI 응답 값들을 조회한다.")
    @Test
    void findExpiredMbtiAnswerIds_ReturnsExpiredAnswersAfterOneMonth() {
        // given, when
        setUpKidWithDeletedMbtiAnswers();
        LocalDateTime referenceDate = LocalDateTime.now().plusMonths(2);

        // then
        List<Long> expiredMbtiAnswerIds = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate);
        assertThat(expiredMbtiAnswerIds).hasSize(2);
    }

    @DisplayName("한 달이 지난 직후 경계 값에서 삭제된 응답을 조회한다.")
    @Test
    void findExpiredMbtiAnswerIds_ReturnsExpiredAnswersAtBoundary() {
        // given, when
        setUpKidWithDeletedMbtiAnswers();
        LocalDateTime referenceDate = LocalDateTime.now().plusMonths(1).plusSeconds(1);

        // then
        List<Long> expiredMbtiAnswerIds = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate);
        assertThat(expiredMbtiAnswerIds).hasSize(2);
    }

    @DisplayName("한 달 이내에 삭제된 MBTI 응답 값들은 조회하지 못한다.")
    @Test
    void findExpiredMbtiAnswerIds_DoesNotReturnAnswersWithinOneMonth() {
        // given, when
        setUpKidWithDeletedMbtiAnswers();
        LocalDateTime referenceDate1 = LocalDateTime.now().plusMonths(1);
        LocalDateTime referenceDate2 = LocalDateTime.now().plusDays(20);

        // then
        List<Long> expiredMbtiAnswerIds1 = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate1);
        List<Long> expiredMbtiAnswerIds2 = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate2);
        assertThat(expiredMbtiAnswerIds1).hasSize(0);
        assertThat(expiredMbtiAnswerIds2).hasSize(0);
    }

    @DisplayName("삭제되지 않은 응답은 한 달이 지나도 조회되지 않는다.")
    @Test
    void findExpiredMbtiAnswerIds_DoesNotReturnNonDeletedAnswersRegardlessOfTime() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        MbtiAnswer mbtiAnswer1 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);
        MbtiAnswer mbtiAnswer2 = createMbtiAnswer(kid, 13, 12, 14, 15, 16, 17, 18, 11);
        MbtiAnswer mbtiAnswer3 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);

        // when
        kidRepository.save(kid);
        mbtiAnswerRepository.saveAll(List.of(mbtiAnswer1, mbtiAnswer2, mbtiAnswer3));
        LocalDateTime referenceDate1 = LocalDateTime.now().plusMonths(1);
        LocalDateTime referenceDate2 = LocalDateTime.now().plusMonths(2);
        List<Long> expiredMbtiAnswerIds1 = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate1);
        List<Long> expiredMbtiAnswerIds2 = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate2);

        // then
        assertThat(expiredMbtiAnswerIds1).hasSize(0);
        assertThat(expiredMbtiAnswerIds2).hasSize(0);
    }

    @DisplayName("한달이 지난 삭제된 데이터를 조회하고 해당 데이터들을 삭제한다.")
    @Test
    void findAndDeleteExpiredMbtiAnswers() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        MbtiAnswer mbtiAnswer1 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);
        MbtiAnswer mbtiAnswer2 = createMbtiAnswer(kid, 13, 12, 14, 15, 16, 17, 18, 11);
        MbtiAnswer mbtiAnswer3 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);
        LocalDateTime referenceDate = LocalDateTime.now().plusMonths(2);
        Pageable pageable = PageRequest.of(0, 2);

        // when
        kidRepository.save(kid);
        mbtiAnswerRepository.saveAll(List.of(mbtiAnswer1, mbtiAnswer2, mbtiAnswer3));
        mbtiAnswer1.delete();
        mbtiAnswer2.delete();
        mbtiAnswer3.delete();
        List<Long> expiredMbtiAnswerIds1 = mbtiAnswerRepository.findExpiredMbtiAnswerIds(referenceDate);
        mbtiAnswerRepository.deleteExpiredMbtiAnswer(expiredMbtiAnswerIds1);
        Page<MbtiAnswer> mbtiAnswers = mbtiAnswerRepository.findMbtiAnswersBy(kid.getId(), pageable);

        // then
        assertThat(mbtiAnswers.getContent()).hasSize(0);
    }

    private void setUpKidWithDeletedMbtiAnswers() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        MbtiAnswer mbtiAnswer1 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);
        MbtiAnswer mbtiAnswer2 = createMbtiAnswer(kid, 13, 12, 14, 15, 16, 17, 18, 11);
        MbtiAnswer mbtiAnswer3 = createMbtiAnswer(kid, 3, 2, 4, 5, 6, 7, 8, 1);

        // when
        kidRepository.save(kid);
        mbtiAnswerRepository.saveAll(List.of(mbtiAnswer1, mbtiAnswer2, mbtiAnswer3));
        mbtiAnswer1.delete();
        mbtiAnswer2.delete();
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