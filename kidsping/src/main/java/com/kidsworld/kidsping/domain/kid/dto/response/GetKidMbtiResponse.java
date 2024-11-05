package com.kidsworld.kidsping.domain.kid.dto.response;

import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKidMbtiResponse {

    private Long kidMbtiId;
    private int eScore;
    private int iScore;
    private int sScore;
    private int nScore;
    private int tScore;
    private int fScore;
    private int jScore;
    private int pScore;
    private MbtiStatus mbtiStatus;

    @Builder
    public GetKidMbtiResponse(Long kidMbtiId, int eScore, int iScore, int sScore, int nScore, int tScore,
                              int fScore, int jScore, int pScore, MbtiStatus mbtiStatus) {
        this.kidMbtiId = kidMbtiId;
        this.eScore = eScore;
        this.iScore = iScore;
        this.sScore = sScore;
        this.nScore = nScore;
        this.tScore = tScore;
        this.fScore = fScore;
        this.jScore = jScore;
        this.pScore = pScore;
        this.mbtiStatus = mbtiStatus;
    }

    public static GetKidMbtiResponse from(KidMbti kidMbti) {
        return GetKidMbtiResponse
                .builder()
                .kidMbtiId(kidMbti.getId())
                .eScore(kidMbti.getEScore())
                .iScore(kidMbti.getIScore())
                .sScore(kidMbti.getSScore())
                .nScore(kidMbti.getNScore())
                .tScore(kidMbti.getTScore())
                .fScore(kidMbti.getFScore())
                .jScore(kidMbti.getJScore())
                .pScore(kidMbti.getPScore())
                .mbtiStatus(kidMbti.getMbtiStatus())
                .build();
    }
}
