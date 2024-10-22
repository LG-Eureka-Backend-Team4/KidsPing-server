package com.kidsworld.kidsping.domain.kid.dto.request;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.question.entity.MBTIResponse;
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

    public static MBTIResponse getMBTIResponse(KidMBTIDiagnosisRequest diagnosisRequest, Kid kid) {
        return MBTIResponse.builder()
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
}