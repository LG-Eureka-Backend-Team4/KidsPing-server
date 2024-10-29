package com.kidsworld.kidsping.domain.kid.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.exception.NotFoundKidException;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class KidServiceImplTest {

    @Autowired
    private KidRepository kidRepository;

    @Autowired
    private KidMbtiHistoryRepository kidMbtiHistoryRepository;

    @Autowired
    private KidService kidService;

    @DisplayName("자녀 성향을 진단하여 자녀의 성향과 성향 히스토리를 생성한다.")
    @Test
    void diagnoseKidMbti_createsKidMbtiAndHistory() {
        // given
        LocalDate nowDate = LocalDate.now();
        Kid kid = createKid(Gender.MALE, "이름1", nowDate);

        Kid saveKid = kidRepository.save(kid);
        KidMbtiDiagnosisRequest kidMbtiDiagonsisRequest = createKidMbtiDiagonsisRequest(saveKid.getId(), 1, 2, 3, 4, 5,
                6, 7, 8);
        kidService.diagnoseKidMbti(kidMbtiDiagonsisRequest);
        Optional<Kid> kidWithMbti = kidRepository.findKidWithMbtiByKidId(kid.getId());
        List<KidMbtiHistory> kidMbtiHistories = kidMbtiHistoryRepository.findTop5ActiveHistories(saveKid);

        // then
        assertThat(kidWithMbti).isPresent();
        KidMbti findKidMbti = kidWithMbti.get().getKidMbti();
        assertThat(findKidMbti)
                .isNotNull()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "jScore", "pScore",
                        "mbtiStatus")
                .contains(
                        1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFP
                );
        assertThat(kidMbtiHistories).hasSize(1)
                .extracting("mbtiStatus")
                .contains(
                        MbtiStatus.INFP
                );
    }

    @DisplayName("자녀의 성향이 바뀌는 재진단을 할 경우 자녀의 성향이 변경된다.")
    @Test
    void reDiagnosis_updatesKidMbtiStatusWhenScoresChange() {
        // given
        LocalDate nowDate = LocalDate.now();
        Kid kid = createKid(Gender.MALE, "이름1", nowDate);

        Kid saveKid = kidRepository.save(kid);
        KidMbtiDiagnosisRequest kidMbtiDiagonsisRequest = createKidMbtiDiagonsisRequest(saveKid.getId(), 1, 2, 3, 4, 5,
                6, 7, 8);
        kidService.diagnoseKidMbti(kidMbtiDiagonsisRequest);

        KidMbtiDiagnosisRequest reDiagnosisRequest = createKidMbtiDiagonsisRequest(saveKid.getId(), 11, 2, 31, 4, 51,
                6, 71, 8);
        kidService.diagnoseKidMbti(reDiagnosisRequest);
        Optional<Kid> kidWithMbti = kidRepository.findKidWithMbtiByKidId(kid.getId());
        List<KidMbtiHistory> kidMbtiHistories = kidMbtiHistoryRepository.findTop5ActiveHistories(saveKid);

        // then
        assertThat(kidWithMbti).isPresent();
        KidMbti findKidMbti = kidWithMbti.get().getKidMbti();
        assertThat(findKidMbti)
                .isNotNull()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "jScore", "pScore",
                        "mbtiStatus")
                .contains(
                        11, 2, 31, 4, 51, 6, 71, 8, MbtiStatus.ESTJ
                );
        assertThat(kidMbtiHistories).hasSize(2)
                .extracting("mbtiStatus")
                .containsExactlyInAnyOrder(
                        MbtiStatus.INFP,
                        MbtiStatus.ESTJ
                );
    }

    @DisplayName("자녀의 성향이 바뀌지 않는 재진단을 할 경우 자녀의 성향은 변경되지 않는다.")
    @Test
    void reDiagnosis_doesNotUpdateKidMbtiStatusWhenScoresUnchanged() {
        // given
        LocalDate nowDate = LocalDate.now();
        Kid kid = createKid(Gender.MALE, "이름1", nowDate);

        Kid saveKid = kidRepository.save(kid);
        KidMbtiDiagnosisRequest kidMbtiDiagonsisRequest = createKidMbtiDiagonsisRequest(saveKid.getId(), 1, 2, 3, 4, 5,
                6, 7, 8);
        kidService.diagnoseKidMbti(kidMbtiDiagonsisRequest);

        KidMbtiDiagnosisRequest reDiagnosisRequest = createKidMbtiDiagonsisRequest(saveKid.getId(), 1, 12, 4, 14, 3,
                16, 5, 18);
        kidService.diagnoseKidMbti(reDiagnosisRequest);
        Optional<Kid> kidWithMbti = kidRepository.findKidWithMbtiByKidId(kid.getId());
        List<KidMbtiHistory> kidMbtiHistories = kidMbtiHistoryRepository.findTop5ActiveHistories(saveKid);

        // then
        assertThat(kidWithMbti).isPresent();
        KidMbti findKidMbti = kidWithMbti.get().getKidMbti();
        assertThat(findKidMbti)
                .isNotNull()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "jScore", "pScore",
                        "mbtiStatus")
                .contains(
                        1, 12, 4, 14, 3, 16, 5, 18, MbtiStatus.INFP
                );
        assertThat(kidMbtiHistories).hasSize(2)
                .extracting("mbtiStatus")
                .containsExactlyInAnyOrder(
                        MbtiStatus.INFP,
                        MbtiStatus.INFP
                );
    }

    
    @DisplayName("처음 진단 시 KidMbti가 생성되고, 재진단 시 동일한 KidMbti가 업데이트된다.")
    @Test
    void diagnoseKidMbti_createsOrUpdateKidMbtiOnFirstAndReDiagnosis() {
        // given
        Kid kid = createKid(Gender.MALE, "테스트아이", LocalDate.now());
        Kid savedKid = kidRepository.save(kid);

        // 첫 진단
        KidMbtiDiagnosisRequest firstDiagnosisRequest = createKidMbtiDiagonsisRequest(savedKid.getId(), 1, 2, 3, 4, 5,
                6, 7, 8);
        kidService.diagnoseKidMbti(firstDiagnosisRequest);

        // 진단 결과 확인 - KidMbti가 생성되었는지 확인
        Optional<Kid> kidWithMbtiAfterFirstDiagnosis = kidRepository.findKidWithMbtiByKidId(savedKid.getId());
        assertThat(kidWithMbtiAfterFirstDiagnosis).isPresent();
        KidMbti kidMbtiAfterFirstDiagnosis = kidWithMbtiAfterFirstDiagnosis.get().getKidMbti();
        assertThat(kidMbtiAfterFirstDiagnosis).isNotNull();
        Long kidMbtiIdAfterFirstDiagnosis = kidMbtiAfterFirstDiagnosis.getId();  // 생성된 KidMbti의 ID를 저장

        // 재진단 (같은 KidMbti가 업데이트되는지 확인하기 위해 다른 점수로 재진단)
        KidMbtiDiagnosisRequest reDiagnosisRequest = createKidMbtiDiagonsisRequest(savedKid.getId(), 10, 9, 8, 7, 6, 5,
                4, 3);
        kidService.diagnoseKidMbti(reDiagnosisRequest);

        // 재진단 결과 확인 - 동일한 KidMbti ID가 업데이트되었는지 확인
        Optional<Kid> kidWithMbtiAfterReDiagnosis = kidRepository.findKidWithMbtiByKidId(savedKid.getId());
        assertThat(kidWithMbtiAfterReDiagnosis).isPresent();
        KidMbti kidMbtiAfterReDiagnosis = kidWithMbtiAfterReDiagnosis.get().getKidMbti();
        assertThat(kidMbtiAfterReDiagnosis).isNotNull();
        assertThat(kidMbtiAfterReDiagnosis.getId()).isEqualTo(kidMbtiIdAfterFirstDiagnosis);  // ID가 동일해야 함

        // 최종 점수 확인 - 재진단한 점수로 업데이트되었는지 확인
        assertThat(kidMbtiAfterReDiagnosis)
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "jScore", "pScore")
                .containsExactly(10, 9, 8, 7, 6, 5, 4, 3);
    }

    @DisplayName("존재하지 않는 자녀로 성향 진단을 할 경우 NotFoundKidException 예외를 반환한다.")
    @Test
    void diagnoseKidMbti_whenKidNotFound_throwsNotFoundKidException() {
        // given
        KidMbtiDiagnosisRequest kidMbtiDiagonsisRequest = createKidMbtiDiagonsisRequest(1L, 1, 2, 3, 4, 5,
                6, 7, 8);

        //  when, then
        assertThatThrownBy(() -> kidService.diagnoseKidMbti(kidMbtiDiagonsisRequest))
                .isInstanceOf(NotFoundKidException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID)
                .hasMessage(ExceptionCode.NOT_FOUND_KID.getMessage());
    }

    private static Kid createKid(Gender gender, String name, LocalDate birth) {
        return Kid.builder()
                .gender(gender)
                .name(name)
                .birth(birth)
                .build();
    }

    private static KidMbtiDiagnosisRequest createKidMbtiDiagonsisRequest(Long kidId, int e, int i, int s, int n, int t,
                                                                         int f, int j, int p) {
        return KidMbtiDiagnosisRequest.builder()
                .kidId(kidId)
                .extraversionScore(e)
                .introversionScore(i)
                .sensingScore(s)
                .intuitionScore(n)
                .thinkingScore(t)
                .feelingScore(f)
                .judgingScore(j)
                .perceivingScore(p)
                .build();
    }
}