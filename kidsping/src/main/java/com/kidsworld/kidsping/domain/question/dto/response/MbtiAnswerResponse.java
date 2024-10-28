package com.kidsworld.kidsping.domain.question.dto.response;

import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MbtiAnswerResponse {

    private Long id;
    private Long kidId;
    private int eScore;
    private int iScore;
    private int sScore;
    private int nScore;
    private int tScore;
    private int fScore;
    private int jScore;
    private int pScore;

    @Builder
    private MbtiAnswerResponse(Long id, Long kidId, int eScore, int iScore, int sScore, int nScore, int tScore,
                               int fScore,
                               int jScore, int pScore) {
        this.id = id;
        this.kidId = kidId;
        this.eScore = eScore;
        this.iScore = iScore;
        this.sScore = sScore;
        this.nScore = nScore;
        this.tScore = tScore;
        this.fScore = fScore;
        this.jScore = jScore;
        this.pScore = pScore;
    }

    public static MbtiAnswerResponse from(MbtiAnswer mbtiAnswer) {
        return MbtiAnswerResponse.builder()
                .id(mbtiAnswer.getId())
                .kidId(mbtiAnswer.getKid().getId())
                .eScore(mbtiAnswer.getEScore())
                .iScore(mbtiAnswer.getIScore())
                .sScore(mbtiAnswer.getSScore())
                .nScore(mbtiAnswer.getNScore())
                .fScore(mbtiAnswer.getFScore())
                .tScore(mbtiAnswer.getTScore())
                .pScore(mbtiAnswer.getPScore())
                .jScore(mbtiAnswer.getJScore())
                .build();
    }
}
