package com.kidsworld.kidsping.domain.kid.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.*;
import com.kidsworld.kidsping.domain.kid.entity.*;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.exception.InvalidRequestFormatException;
import com.kidsworld.kidsping.domain.kid.exception.NotFoundKidException;
import com.kidsworld.kidsping.domain.kid.repository.KidBadgeAwardedRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import com.kidsworld.kidsping.domain.question.repository.MbtiAnswerRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import com.kidsworld.kidsping.global.common.entity.UploadedFile;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.infra.s3.FileStore;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

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

    @Autowired
    private KidMbtiRepository kidMbtiRepository;

    @Autowired
    private MbtiAnswerRepository mbtiAnswerRepository;

    @Autowired
    private EntityManager entityManager;

    //시은
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileStore fileStore;

    @Value("${cloud.aws.s3.default-kid-profile}")
    private String defaultKidProfile;

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


    @DisplayName("자녀 삭제 시 자녀와 연관되어 있는 데이터들이 soft delete 된다.")
    @Test
    void softDeleteKidAndRelatedData() {
        // given
        Kid kid = createKid(Gender.MALE, "테스트아이", LocalDate.now());
        Kid savedKid = kidRepository.save(kid);
        KidMbti kidMbti = createKidMbti(1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.ENFJ);
        kid.updateKidMbti(kidMbti);
        kidMbtiRepository.save(kidMbti);

        KidMbtiHistory kidMbtiHistory = createKidMbtiHistory(kid);
        MbtiAnswer mbtiAnswer = createMbtiAnswer(kid, 1, 2, 3, 4, 5, 6, 7, 8);

        // when
        kidMbtiHistoryRepository.save(kidMbtiHistory);
        mbtiAnswerRepository.save(mbtiAnswer);
        kidService.deleteKid(savedKid.getId());

        // Hibernate 캐시 플러시 및 클리어
        entityManager.flush();
        entityManager.clear();

        // then
        Kid deletedKid = kidRepository.findById(kid.getId()).orElse(null);
        KidMbti deletedKidMbti = kidMbtiRepository.findById(kidMbti.getId()).orElse(null);
        KidMbtiHistory deletedKidMbtiHistory = kidMbtiHistoryRepository.findById(kidMbtiHistory.getId()).orElse(null);
        MbtiAnswer deletedMbtiAnswer = mbtiAnswerRepository.findById(mbtiAnswer.getId()).orElse(null);

        assertThat(deletedKid).isNotNull();
        assertThat(deletedKid.isDeleted()).isTrue();

        assertThat(deletedKidMbti).isNotNull();
        assertThat(deletedKidMbti.getIsDeleted()).isTrue();

        assertThat(deletedKidMbtiHistory).isNotNull();
        assertThat(deletedKidMbtiHistory.isDeleted()).isTrue();

        assertThat(deletedMbtiAnswer).isNotNull();
        assertThat(deletedMbtiAnswer.getIsDeleted()).isTrue();
    }



//    @DisplayName("이미지 파일과 함께 자녀 프로필을 성공적으로 생성한다")
//    @Test
//    void 자녀프로필생성_성공() throws JsonProcessingException {
//        // given
//        User user = User.builder()
//                .email("test@test.com")
//                .password("password")
//                .userName("testUser")
//                .isDeleted(false)
//                .build();
//        User savedUser = userRepository.save(user);
//
//        String kidName = "김영자";
//        String gender = "FEMALE";
//        String birth = LocalDate.now().toString();
//
//        MockMultipartFile profileImage = new MockMultipartFile(
//                "profileImage", "sample.jpg", "image/jpeg", "test image content".getBytes()
//        );
//
//        UploadedFile uploadedFile = new UploadedFile(
//                "sample.jpg", defaultKidProfile
//        );
//
//        when(fileStore.storeFiles(anyList(), any())).thenReturn(List.of(uploadedFile));
//
//        CreateKidRequest request = CreateKidRequest.builder()
//                .kidName(kidName)
//                .gender(gender)
//                .birth(birth)
//                .userId(savedUser.getId())
//                .build();
//
//        String requestJson = objectMapper.writeValueAsString(request);
//
//        // when
//        CreateKidResponse response = kidService.createKid(requestJson, profileImage);
//
//        // then
//        assertThat(response).isNotNull();
//        assertThat(response.getKidId()).isNotNull();
//    }



    @DisplayName("자녀 프로필 기본 정보를 성공적으로 수정한다")
    @Test
    void 자녀프로필수정_성공() {
        // given
        String originalName = "김영자";
        String updatedName = "김순자";
        LocalDate originalBirth = LocalDate.of(2020, 1, 1);
        LocalDate updatedBirth = LocalDate.of(2020, 12, 31);

        User user = User.builder()
                .id(1L)
                .build();
        userRepository.save(user);

        Kid kid = Kid.builder()
                .gender(Gender.MALE)
                .name(originalName)
                .birth(originalBirth)
                .isDeleted(false)
                .user(user)
                .uploadedFile(new UploadedFile("sample.jpg", defaultKidProfile))
                .build();

        Kid savedKid = kidRepository.save(kid);

        UpdateKidRequest updateRequest = UpdateKidRequest.builder()
                .kidName(updatedName)
                .gender("FEMALE")
                .birth(updatedBirth.toString())
                .build();

        // when
        UpdateKidResponse response = kidService.updateKid(savedKid.getId(), updateRequest, null);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getKidId()).isEqualTo(savedKid.getId());

        Kid updatedKid = kidRepository.findById(savedKid.getId()).orElseThrow();
        assertThat(updatedKid)
                .extracting("name", "gender", "birth")
                .containsExactly(updatedName, Gender.FEMALE, updatedBirth);
    }


    @DisplayName("자녀 프로필을 성공적으로 조회한다")
    @Test
    void 자녀프로필조회_성공() {
        // given
        String kidName = "김영자";
        LocalDate birth = LocalDate.now();
        Gender gender = Gender.MALE;

        User user = User.builder()
                .id(1L)
                .build();
        userRepository.save(user);

        Kid kid = Kid.builder()
                .gender(gender)
                .name(kidName)
                .birth(birth)
                .isDeleted(false)
                .user(user)
                .uploadedFile(new UploadedFile("sample.jpg", defaultKidProfile))
                .build();

        KidMbti kidMbti = createKidMbti(10, 5, 8, 7, 6, 9, 11, 4, MbtiStatus.ENFP);
        KidMbti savedKidMbti = kidMbtiRepository.save(kidMbti);
        kid.updateKidMbti(savedKidMbti);

        Kid savedKid = kidRepository.save(kid);

        // when
        GetKidWithMbtiAndBadgeResponse response = kidService.getKid(savedKid.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response)
                .extracting("kidId", "kidName", "gender", "birth", "mbtiStatus")
                .containsExactly(savedKid.getId(), kidName, gender, birth, MbtiStatus.ENFP);

        assertThat(response.getKidBadgeAwardedResponses()).isEmpty();

        assertThat(response)
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "jScore", "pScore")
                .containsExactly(10, 5, 8, 7, 6, 9, 4, 11);

        assertThat(response.getKidMbtiId()).isEqualTo(savedKidMbti.getId());
    }


    @Test
    @DisplayName("자녀의 최근 5개 성향 히스토리를 조회한다")
    void 자녀성향5개_조회() {
        // Given
        User user = User.builder()
                .email("test@test.com")
                .password("password")
                .userName("testUser")
                .build();
        userRepository.save(user);

        Kid kid = Kid.builder()
                .name("김영자")
                .gender(Gender.MALE)
                .birth(LocalDate.of(2020, 1, 1))
                .user(user)
                .isDeleted(false)
                .build();
        Kid savedKid = kidRepository.save(kid);

        for (int i = 0; i < 6; i++) {
            KidMbtiHistory history = createKidMbtiHistory(savedKid);
            kidMbtiHistoryRepository.save(history);
        }

        entityManager.flush();
        entityManager.clear();

        // When
        List<GetKidMbtiHistoryResponse> result = kidService.getKidMbtiHistory(kid.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(5);

        result.forEach(history -> {
            assertThat(history.getMbtiStatus()).isEqualTo("INFJ");
        });
    }



    private static KidMbtiHistory createKidMbtiHistory(Kid kid) {
        return KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(MbtiStatus.INFJ)
                .isDeleted(false)
                .build();
    }

    private static Kid createKid(Gender gender, String name, LocalDate birth) {
        return Kid.builder()
                .gender(gender)
                .name(name)
                .birth(birth)
                .build();
    }

    private static KidMbti createKidMbti(int e, int i, int s, int n, int t, int f, int p, int j,
                                         MbtiStatus mbtiStatus) {
        return KidMbti
                .builder()
                .eScore(e)
                .iScore(i)
                .sScore(s)
                .nScore(n)
                .tScore(t)
                .fScore(f)
                .pScore(p)
                .jScore(j)
                .mbtiStatus(mbtiStatus)
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