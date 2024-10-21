package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiScore;
import com.kidsworld.kidsping.domain.mbti.entity.enums.MbtiStatus;
import com.kidsworld.kidsping.domain.mbti.entity.enums.PersonalityTrait;
import com.kidsworld.kidsping.domain.mbti.repository.MBTIScoreRepository;
import com.kidsworld.kidsping.domain.question.entity.MbtiResponse;
import com.kidsworld.kidsping.domain.question.repository.MbtiResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KidServiceImpl implements KidService {

    private final KidRepository kidRepository;
    private final MbtiResponseRepository mbtiResponseRepository;
    private final MBTIScoreRepository mbtiScoreRepository;
    private final KidMBTIRepository kidMBTIRepository;
    private final KidMBTIHistoryRepository kidMBTIHistoryRepository;

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