package com.kidsworld.kidsping.domain.kid.dto.request;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiScore;
import com.kidsworld.kidsping.domain.question.entity.MbtiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidMBTIDiagnosisRequest {

    private Long userId;
    private int extraversionScore;
    private int introversionScore;
    private int sensingScore;
    private int intuitionScore;
    private int thinkingScore;
    private int feelingScore;
    private int judgingScore;
    private int perceivingScore;

    public static MbtiResponse getMBTIResponse(KidMBTIDiagnosisRequest diagnosisRequest, Kid kid) {
        return MbtiResponse.builder()
                .kid(kid)
                .eScore(diagnosisRequest.getExtraversionScore())
                .iScore(diagnosisRequest.getIntroversionScore())
                .sScore(diagnosisRequest.getSensingScore())
                .nScore(diagnosisRequest.getIntuitionScore())
                .fScore(diagnosisRequest.getFeelingScore())
                .tScore(diagnosisRequest.getThinkingScore())
                .jScore(diagnosisRequest.getJudgingScore())
                .pScore(diagnosisRequest.getPerceivingScore())
                .isDeleted(false)
                .build();
    }

    public static MbtiScore getMBTIScore(KidMBTIDiagnosisRequest diagnosisRequest) {
        return MbtiScore.builder()
                .eScore(diagnosisRequest.getExtraversionScore())
                .iScore(diagnosisRequest.getIntroversionScore())
                .sScore(diagnosisRequest.getSensingScore())
                .nScore(diagnosisRequest.getIntuitionScore())
                .fScore(diagnosisRequest.getFeelingScore())
                .tScore(diagnosisRequest.getThinkingScore())
                .jScore(diagnosisRequest.getJudgingScore())
                .pScore(diagnosisRequest.getPerceivingScore())
                .isDeleted(false)
                .build();
    }
}