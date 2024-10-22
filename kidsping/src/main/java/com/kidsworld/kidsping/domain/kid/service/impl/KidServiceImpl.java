package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.dto.request.KidCreateRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidUpdateRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.KidCreateResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidDeleteResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidGetResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidUpdateResponse;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIRepository;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiScore;
import com.kidsworld.kidsping.domain.mbti.entity.enums.MbtiStatus;
import com.kidsworld.kidsping.domain.mbti.entity.enums.PersonalityTrait;
import com.kidsworld.kidsping.domain.mbti.repository.MBTIScoreRepository;
import com.kidsworld.kidsping.domain.question.entity.MbtiResponse;
import com.kidsworld.kidsping.domain.question.repository.MbtiResponseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KidServiceImpl implements KidService {

    private final KidRepository kidRepository;
    private final UserRepository userRepository;
    private final MbtiResponseRepository mbtiResponseRepository;
    private final MBTIScoreRepository mbtiScoreRepository;
    private final KidMBTIRepository kidMBTIRepository;
    private final KidMBTIHistoryRepository kidMBTIHistoryRepository;


    /*
    자녀 프로필 생성
    */
    @Override
    @Transactional
    public KidCreateResponse createKid(KidCreateRequest request) {
        long kidCount = kidRepository.countByUserId(request.getUserId());
        if (kidCount >= 5) {
            throw new IllegalStateException("최대 5명의 자녀만 등록할 수 있습니다.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        Kid kid = Kid.builder()
                .gender(Gender.valueOf(request.getGender()))
                .name(request.getKidName())
                .birth(LocalDate.parse(request.getBirth()))
                .isDeleted(false)
                .user(user)
                .build();

        Kid savedKid = kidRepository.save(kid);

        return KidCreateResponse.from(savedKid);
    }

    /*
    자녀 프로필 조회
    */
    @Override
    @Transactional
    public KidGetResponse getKid(Long kidId) {
        Kid kid = findKidOrThrow(kidId);
        return new KidGetResponse(kid);
    }

    /*
    자녀 프로필 수정
    */
    @Override
    @Transactional
    public KidUpdateResponse updateKid(Long kidId, KidUpdateRequest request) {
        Kid kid = findKidOrThrow(kidId);

        kid.update(
                Gender.valueOf(request.getGender()),
                request.getKidName(),
                LocalDate.parse(request.getBirth())
        );

        return KidUpdateResponse.from(kid);
    }

    /*
    자녀 프로필 삭제
    */
    @Override
    @Transactional
    public KidDeleteResponse deleteKid(Long kidId) {
        Kid kid = findKidOrThrow(kidId);
        kidRepository.delete(kid);
        return new KidDeleteResponse(kidId);
    }

    private Kid findKidOrThrow(Long kidId) {
        return kidRepository.findById(kidId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 자녀를 찾을 수 없습니다: " + kidId));
    }

    @Transactional
    @Override
    public void diagnoseKidMBTI(KidMBTIDiagnosisRequest diagnosisRequest) {
        Kid kid = findKid(diagnosisRequest);

        MbtiResponse mbtiResponse = KidMBTIDiagnosisRequest.getMBTIResponse(diagnosisRequest, kid);
        mbtiResponseRepository.save(mbtiResponse);

        MbtiScore mbtiScore = KidMBTIDiagnosisRequest.getMBTIScore(diagnosisRequest);
        mbtiScoreRepository.save(mbtiScore);

        MbtiStatus mbtiStatus = calculateMbtiStatus(diagnosisRequest);

        KidMbti kidMbti = saveKidMBTI(mbtiStatus, mbtiScore);

        kid.updateKidMbti(kidMbti);

        saveKidMBTIHistory(kid, mbtiStatus);
    }

    private Kid findKid(KidMBTIDiagnosisRequest diagnosisRequest) {
        return kidRepository.findById(diagnosisRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("no kid"));
    }

    private MbtiStatus calculateMbtiStatus(KidMBTIDiagnosisRequest request) {
        String mbti = compareScores(request.getExtraversionScore(), request.getIntroversionScore(),
                PersonalityTrait.EXTRAVERSION.getType(), PersonalityTrait.INTROVERSION.getType())
                + compareScores(request.getSensingScore(), request.getIntuitionScore(),
                PersonalityTrait.SENSING.getType(), PersonalityTrait.INTUITION.getType())
                + compareScores(request.getFeelingScore(), request.getThinkingScore(),
                PersonalityTrait.FEELING.getType(), PersonalityTrait.THINKING.getType())
                + compareScores(request.getPerceivingScore(), request.getJudgingScore(),
                PersonalityTrait.PERCEIVING.getType(), PersonalityTrait.JUDGING.getType());
        return MbtiStatus.toMbtiStatus(mbti);
    }

    private String compareScores(int firstScore, int secondScore, String firstType, String secondType) {
        return firstScore >= secondScore ? firstType : secondType;
    }

    private KidMbti saveKidMBTI(MbtiStatus mbtiStatus, MbtiScore mbtiScore) {
        KidMbti kidMbti = KidMbti.builder()
                .isDeleted(false)
                .mbtiStatus(mbtiStatus)
                .mbtiScore(mbtiScore)
                .build();
        return kidMBTIRepository.save(kidMbti);
    }

    private void saveKidMBTIHistory(Kid kid, MbtiStatus mbtiStatus) {
        KidMbtiHistory kidMbtiHistory = KidMbtiHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMBTIHistoryRepository.save(kidMbtiHistory);
    }
}