package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMBTI;
import com.kidsworld.kidsping.domain.kid.entity.KidMBTIHistory;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIHistoryRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidMBTIRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.mbti.entity.enums.MbtiStatus;
import com.kidsworld.kidsping.domain.mbti.entity.enums.PersonalityTrait;
import com.kidsworld.kidsping.domain.question.entity.MBTIResponse;
import com.kidsworld.kidsping.domain.question.repository.MBTIResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KidServiceImpl implements KidService {

    private final KidRepository kidRepository;
    private final MBTIResponseRepository mbtiResponseRepository;
    private final KidMBTIRepository kidMBTIRepository;
    private final KidMBTIHistoryRepository kidMBTIHistoryRepository;

    @Transactional
    @Override
    public void diagnoseKidMBTI(KidMBTIDiagnosisRequest diagnosisRequest) {
        Kid kid = findKidById(diagnosisRequest);

        saveMBTIResponse(diagnosisRequest, kid);

        MbtiStatus mbtiStatus = calculateMbtiStatus(diagnosisRequest);

        updateOrCreateKidMbti(kid, diagnosisRequest, mbtiStatus);

        saveKidMBTIHistory(kid, mbtiStatus);
    }

    private Kid findKidById(KidMBTIDiagnosisRequest diagnosisRequest) {
        return kidRepository.findById(diagnosisRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("no kid"));
    }

    private void saveMBTIResponse(KidMBTIDiagnosisRequest diagnosisRequest, Kid kid) {
        MBTIResponse mbtiResponse = KidMBTIDiagnosisRequest.getMBTIResponse(diagnosisRequest, kid);
        mbtiResponseRepository.save(mbtiResponse);
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

    private void updateOrCreateKidMbti(Kid kid, KidMBTIDiagnosisRequest diagnosisRequest, MbtiStatus mbtiStatus) {
        KidMBTI kidMbti = kid.getKidMbti();
        if (kidMbti == null) {
            kidMbti = createKidMbti(diagnosisRequest, mbtiStatus);
        } else {
            updateKidMbti(kidMbti, diagnosisRequest, mbtiStatus);
        }
        kid.updateKidMbti(kidMbti);
    }

    private KidMBTI createKidMbti(KidMBTIDiagnosisRequest diagnosisRequest, MbtiStatus mbtiStatus) {
        KidMBTI kidMbti = KidMBTI.builder()
                .eScore(diagnosisRequest.getExtraversionScore())
                .iScore(diagnosisRequest.getIntroversionScore())
                .sScore(diagnosisRequest.getSensingScore())
                .nScore(diagnosisRequest.getIntuitionScore())
                .fScore(diagnosisRequest.getFeelingScore())
                .tScore(diagnosisRequest.getThinkingScore())
                .jScore(diagnosisRequest.getJudgingScore())
                .pScore(diagnosisRequest.getPerceivingScore())
                .mbtiStatus(mbtiStatus)
                .build();
        return kidMBTIRepository.save(kidMbti);
    }

    private void updateKidMbti(KidMBTI kidMbti, KidMBTIDiagnosisRequest diagnosisRequest, MbtiStatus mbtiStatus) {
        kidMbti.updateMBTIScore(
                diagnosisRequest.getExtraversionScore(),
                diagnosisRequest.getIntroversionScore(),
                diagnosisRequest.getSensingScore(),
                diagnosisRequest.getIntuitionScore(),
                diagnosisRequest.getThinkingScore(),
                diagnosisRequest.getFeelingScore(),
                diagnosisRequest.getJudgingScore(),
                diagnosisRequest.getPerceivingScore(),
                mbtiStatus
        );
    }

    private void saveKidMBTIHistory(Kid kid, MbtiStatus mbtiStatus) {
        KidMBTIHistory kidMbtiHistory = KidMBTIHistory.builder()
                .kid(kid)
                .mbtiStatus(mbtiStatus)
                .isDeleted(false)
                .build();
        kidMBTIHistoryRepository.save(kidMbtiHistory);
    }
}