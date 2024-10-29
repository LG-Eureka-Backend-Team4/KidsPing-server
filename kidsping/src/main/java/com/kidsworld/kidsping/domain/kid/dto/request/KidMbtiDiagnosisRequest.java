package com.kidsworld.kidsping.domain.kid.dto.request;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidMbtiDiagnosisRequest {

    private Long kidId;
    private int extraversionScore;
    private int introversionScore;
    private int sensingScore;
    private int intuitionScore;
    private int thinkingScore;
    private int feelingScore;
    private int judgingScore;
    private int perceivingScore;

    @Builder
    private KidMbtiDiagnosisRequest(Long kidId, int extraversionScore, int introversionScore, int sensingScore,
                                    int intuitionScore, int thinkingScore, int feelingScore, int judgingScore,
                                    int perceivingScore) {
        this.kidId = kidId;
        this.extraversionScore = extraversionScore;
        this.introversionScore = introversionScore;
        this.sensingScore = sensingScore;
        this.intuitionScore = intuitionScore;
        this.thinkingScore = thinkingScore;
        this.feelingScore = feelingScore;
        this.judgingScore = judgingScore;
        this.perceivingScore = perceivingScore;
    }

    public static MbtiAnswer getMbtiAnswer(KidMbtiDiagnosisRequest diagnosisRequest, Kid kid) {
        return MbtiAnswer.builder()
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