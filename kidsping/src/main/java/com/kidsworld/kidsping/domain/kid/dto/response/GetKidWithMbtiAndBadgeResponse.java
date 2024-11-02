package com.kidsworld.kidsping.domain.kid.dto.response;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKidWithMbtiAndBadgeResponse {

    private Long kidId;
    private Long kidMbtiId;
    private String kidName;
    private Gender gender;
    private LocalDate birth;
    private int eScore;
    private int iScore;
    private int sScore;
    private int nScore;
    private int tScore;
    private int fScore;
    private int jScore;
    private int pScore;
    private MbtiStatus mbtiStatus;
    private List<KidBadgeAwardedResponse> kidBadgeAwardedResponses;

    @Builder

    private GetKidWithMbtiAndBadgeResponse(Long kidId, Long kidMbtiId, String kidName, Gender gender,
                                           LocalDate birth,
                                           int eScore, int iScore, int sScore, int nScore, int tScore, int fScore,
                                           int jScore,
                                           int pScore, MbtiStatus mbtiStatus,
                                           List<KidBadgeAwardedResponse> kidBadgeAwardeds) {
        this.kidId = kidId;
        this.kidMbtiId = kidMbtiId;
        this.kidName = kidName;
        this.gender = gender;
        this.birth = birth;
        this.eScore = eScore;
        this.iScore = iScore;
        this.sScore = sScore;
        this.nScore = nScore;
        this.tScore = tScore;
        this.fScore = fScore;
        this.jScore = jScore;
        this.pScore = pScore;
        this.mbtiStatus = mbtiStatus;
        this.kidBadgeAwardedResponses = kidBadgeAwardeds;
    }

    public static GetKidWithMbtiAndBadgeResponse createKidWithMbtiAndBadgeResponse(Kid kid,
                                                                                   List<KidBadgeAwardedResponse> kidBadgeAwardeds) {
        return GetKidWithMbtiAndBadgeResponse.builder()
                .kidId(kid.getId())
                .kidMbtiId(kid.getKidMbti().getId())
                .kidName(kid.getName())
                .gender(kid.getGender())
                .birth(kid.getBirth())
                .eScore(kid.getKidMbti().getEScore())
                .iScore(kid.getKidMbti().getIScore())
                .sScore(kid.getKidMbti().getSScore())
                .nScore(kid.getKidMbti().getNScore())
                .tScore(kid.getKidMbti().getTScore())
                .fScore(kid.getKidMbti().getFScore())
                .pScore(kid.getKidMbti().getPScore())
                .jScore(kid.getKidMbti().getJScore())
                .mbtiStatus(kid.getKidMbti().getMbtiStatus())
                .kidBadgeAwardeds(kidBadgeAwardeds)
                .build();
    }
}
