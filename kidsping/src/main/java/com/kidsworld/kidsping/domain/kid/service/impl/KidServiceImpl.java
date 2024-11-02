package com.kidsworld.kidsping.domain.kid.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.CreateKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.DeleteKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidMbtiHistoryResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.UpdateKidResponse;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidBadgeAwarded;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.exception.InvalidRequestFormatException;
import com.kidsworld.kidsping.domain.kid.exception.MaxKidLimitReachedException;
import com.kidsworld.kidsping.domain.kid.exception.NotFoundKidException;
import com.kidsworld.kidsping.domain.kid.repository.KidBadgeAwardedRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.like.repository.LikeMbtiRepository;
import com.kidsworld.kidsping.domain.like.service.LikeGenreService;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import com.kidsworld.kidsping.domain.question.repository.MbtiAnswerRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.UnauthorizedUserException;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import com.kidsworld.kidsping.global.common.entity.MbtiScore;
import com.kidsworld.kidsping.global.common.entity.UploadedFile;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.util.MbtiCalculator;
import com.kidsworld.kidsping.infra.s3.FileStore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KidServiceImpl implements KidService {

    private final KidRepository kidRepository;
    private final MbtiAnswerRepository mbtiAnswerRepository;
    private final UserRepository userRepository;
    private final KidMbtiRepository kidMBTIRepository;
    private final KidMbtiHistoryRepository kidMBTIHistoryRepository;
    private final LikeGenreService likeGenreService;
    private final GenreScoreService genreScoreService;
    private final LikeMbtiService likeMbtiService;
    private final KidBadgeAwardedRepository kidBadgeAwardedRepository;
    private final LikeMbtiRepository likeMbtiRepository;
    private final FileStore fileStore;
    private final ObjectMapper objectMapper;

    @Value("${cloud.aws.s3.default-kid-profile}")
    private String defaultProfileImage;


    /*
    자녀 프로필 생성
    */
    @Override
    @Transactional
    public CreateKidResponse createKid(String request, MultipartFile profileImage) {
        CreateKidRequest kidRequest;
        try {
            kidRequest = objectMapper.readValue(request, CreateKidRequest.class);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestFormatException();
        }

        User user = userRepository.findById(kidRequest.getUserId())
                .orElseThrow(UnauthorizedUserException::new);

        long userKidCount = kidRepository.countByUserId(user.getId());
        if (userKidCount >= 5) {
            throw new MaxKidLimitReachedException();
        }

        // 프로필 이미지 처리
        UploadedFile uploadedFile;
        if (profileImage != null && !profileImage.isEmpty()) {
            List<UploadedFile> uploadedFiles = fileStore.storeFiles(List.of(profileImage), FileStore.KID_PROFILE_DIR);
            uploadedFile = uploadedFiles.isEmpty() ?
                    new UploadedFile("default-profile.png", defaultProfileImage) :
                    uploadedFiles.get(0);
        } else {
            uploadedFile = new UploadedFile("default-profile.png", defaultProfileImage);
        }

        Kid kid = Kid.builder()
                .gender(Gender.valueOf(kidRequest.getGender()))
                .name(kidRequest.getKidName())
                .birth(LocalDate.parse(kidRequest.getBirth()))
                .isDeleted(false)
                .user(user)
                .uploadedFile(uploadedFile)
                .build();

        Kid savedKid = kidRepository.save(kid);

        return CreateKidResponse.from(savedKid);
    }


    /*
    자녀 프로필 조회
    */
    @Override
    public GetKidResponse getKid(Long kidId) {

        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);

        return new GetKidResponse(kid);
    }

    /*
    자녀 프로필 수정
    */
    @Override
    @Transactional
    public UpdateKidResponse updateKid(Long kidId, UpdateKidRequest request, MultipartFile profileImage) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);

        // 프로필 이미지 처리
        UploadedFile uploadedFile = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            List<UploadedFile> uploadedFiles = fileStore.storeFiles(List.of(profileImage), FileStore.KID_PROFILE_DIR);
            if (!uploadedFiles.isEmpty()) {
                uploadedFile = uploadedFiles.get(0);
            }
        }

        kid.update(
                Gender.valueOf(request.getGender()),
                request.getKidName(),
                LocalDate.parse(request.getBirth()),
                uploadedFile
        );

        return UpdateKidResponse.from(kid);
    }

    /*
    자녀 프로필 삭제
    */
    @Override
    @Transactional
    public DeleteKidResponse deleteKid(Long kidId) {
        if (!kidRepository.existsById(kidId)) {
            throw new NotFoundKidException();
        }
        kidRepository.softDeleteKidAndRelatedData(kidId);
        // 자녀 장르 점수, 도서 좋아요, 장르 좋아요 초기화
        genreScoreService.resetGenreScoreForKid(kidId);
        likeGenreService.resetGenreLikesForKid(kidId);
        likeMbtiService.resetMbtiLikesForKid(kidId);

        return new DeleteKidResponse(kidId);
    }

    /**
     * 자녀의 MBTI를 진단하는 메서드
     *
     * @param diagnosisRequest 자녀의 MBTI 점수 데이터를 담은 KidMbtiDiagnosisRequest 객체
     */
    @Transactional
    @Override
    public void diagnoseKidMbti(KidMbtiDiagnosisRequest diagnosisRequest) {
        Kid kid = findKidById(diagnosisRequest.getKidId());
        saveMbtiResponse(diagnosisRequest, kid);

        MbtiScore diagnosedKidMbtiScore = MbtiScore.from(diagnosisRequest);
        MbtiStatus updatedMbtiStatus = MbtiCalculator.determineMbtiType(diagnosedKidMbtiScore);
        updateOrCreateKidMbti(kid, diagnosisRequest, updatedMbtiStatus);

        saveKidMbtiHistory(kid, updatedMbtiStatus);
    }

    /**
     * 자녀 엔티티를 조회하는 메서드.
     *
     * @param kidId 자녀 엔티티의 id 값
     */
    private Kid findKidById(Long kidId) {
        return kidRepository.findKidBy(kidId)
                .orElseThrow(NotFoundKidException::new);
    }

    /**
     * 자녀가 응답한 MBTI 설문 결과를 저장하는 메서드
     *
     * @param diagnosisRequest 자녀의 MBTI 점수 데이터를 담은 KidMbtiDiagnosisRequest 객체
     * @param kid              자녀 엔티티
     */
    private void saveMbtiResponse(KidMbtiDiagnosisRequest diagnosisRequest, Kid kid) {
        MbtiAnswer mbtiAnswer = KidMbtiDiagnosisRequest.getMbtiAnswer(diagnosisRequest, kid);
        mbtiAnswerRepository.save(mbtiAnswer);
    }

    /**
     * 자녀의 현재 MBTI를 조회하여 null 인경우 새로 생성하고 null 이 아니면 기존 자녀의 MBTI를 변경하는 메서드 자녀의 현재 MBTI(currentKidMbti)가 null 이면 처음 진단
     * 자녀의 현재 MBTI(currentKidMbti)가 null 이 아니면 재진단
     *
     * @param kid               자녀 엔티티
     * @param diagnosisRequest  자녀의 MBTI 점수 데이터를 담은 KidMbtiDiagnosisRequest 객체
     * @param updatedMbtiStatus 설문 결과를 바탕으로 업데이트 된 자녀 MBTI 상태 객체
     */
    private void updateOrCreateKidMbti(Kid kid, KidMbtiDiagnosisRequest diagnosisRequest,
                                       MbtiStatus updatedMbtiStatus) {
        KidMbti currentKidMbti = kid.getKidMbti();
        if (currentKidMbti == null) {
            currentKidMbti = createKidMbti(diagnosisRequest, updatedMbtiStatus);
        } else {
            MbtiScore mbtiScore = MbtiScore.from(diagnosisRequest);
            currentKidMbti.updateMbti(mbtiScore, updatedMbtiStatus);
            // 자녀 장르 점수, 도서 좋아요, 장르 좋아요 초기화
            genreScoreService.resetGenreScoreForKid(kid.getId());
            likeGenreService.resetGenreLikesForKid(kid.getId());
            likeMbtiService.resetMbtiLikesForKid(kid.getId());
        }
        kid.updateKidMbti(currentKidMbti);
    }

    /**
     * 자녀의 MBTI를 생성하는 메서드
     *
     * @param diagnosisRequest  자녀의 MBTI 점수 데이터를 담은 KidMbtiDiagnosisRequest 객체
     * @param updatedMbtiStatus 설문 결과를 바탕으로 업데이트 된 자녀 MBTI 상태 객체
     */
    private KidMbti createKidMbti(KidMbtiDiagnosisRequest diagnosisRequest, MbtiStatus updatedMbtiStatus) {
        KidMbti kidMbti = KidMbti.builder()
                .eScore(diagnosisRequest.getExtraversionScore())
                .iScore(diagnosisRequest.getIntroversionScore())
                .sScore(diagnosisRequest.getSensingScore())
                .nScore(diagnosisRequest.getIntuitionScore())
                .fScore(diagnosisRequest.getFeelingScore())
                .tScore(diagnosisRequest.getThinkingScore())
                .jScore(diagnosisRequest.getJudgingScore())
                .pScore(diagnosisRequest.getPerceivingScore())
                .mbtiStatus(updatedMbtiStatus)
                .build();
        return kidMBTIRepository.save(kidMbti);
    }

    /**
     * 자녀의 MBTI 히스토리를 생성하는 메서드
     *
     * @param kid               자녀 엔티티
     * @param updatedMbtiStatus 설문 결과를 바탕으로 업데이트 된 자녀 MBTI 상태 객체
     */
    private void saveKidMbtiHistory(Kid kid, MbtiStatus updatedMbtiStatus) {
        KidMbtiHistory kidMbtiHistory = KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(updatedMbtiStatus)
                .isDeleted(false)
                .build();
        kidMBTIHistoryRepository.save(kidMbtiHistory);
    }

    /*
    자녀 성향 히스토리 조회
    */
    @Override
    public List<GetKidMbtiHistoryResponse> getKidMbtiHistory(Long kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(NotFoundKidException::new);

        List<KidMbtiHistory> histories = kidMBTIHistoryRepository.findTop5ActiveHistories(kid);

        return histories.stream()
                .map(GetKidMbtiHistoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<KidBadgeAwarded> getAwardedBadges(Long kidId) {
        return kidBadgeAwardedRepository.findAllByKidId(kidId);
    }

    @Override
    public void deleteExpiredKid() {
        kidRepository.deleteExpiredKid(LocalDateTime.now());
    }
}