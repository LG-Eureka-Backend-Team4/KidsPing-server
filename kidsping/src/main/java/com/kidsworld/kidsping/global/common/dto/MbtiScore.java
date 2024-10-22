package com.kidsworld.kidsping.global.common.dto;

import com.kidsworld.kidsping.domain.book.entity.BookMBTI;
import com.kidsworld.kidsping.domain.kid.entity.KidMBTI;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MbtiScore {

    private int eScore;
    private int iScore;
    private int sScore;
    private int nScore;
    private int tScore;
    private int fScore;
    private int jScore;
    private int pScore;

    @Builder
    private MbtiScore(int eScore, int iScore, int sScore, int nScore, int tScore, int fScore, int jScore, int pScore) {
        this.eScore = eScore;
        this.iScore = iScore;
        this.sScore = sScore;
        this.nScore = nScore;
        this.tScore = tScore;
        this.fScore = fScore;
        this.jScore = jScore;
        this.pScore = pScore;
    }

    public static MbtiScore from(KidMBTI kidMBTI) {
        return MbtiScore.builder()
                .eScore(kidMBTI.getEScore())
                .iScore(kidMBTI.getIScore())
                .sScore(kidMBTI.getSScore())
                .nScore(kidMBTI.getNScore())
                .tScore(kidMBTI.getTScore())
                .fScore(kidMBTI.getFScore())
                .jScore(kidMBTI.getJScore())
                .pScore(kidMBTI.getPScore())
                .build();
    }

    public void updateMbtiScore(BookMBTI bookMBTI, LikeStatus previousStatus, LikeStatus currentStatus) {
        int multiplier = calculateMultiplier(previousStatus, currentStatus);

        this.eScore += multiplier * bookMBTI.getEScore();
        this.iScore += multiplier * bookMBTI.getIScore();
        this.sScore += multiplier * bookMBTI.getSScore();
        this.nScore += multiplier * bookMBTI.getNScore();
        this.tScore += multiplier * bookMBTI.getTScore();
        this.fScore += multiplier * bookMBTI.getFScore();
        this.jScore += multiplier * bookMBTI.getJScore();
        this.pScore += multiplier * bookMBTI.getPScore();
    }

    /*
     * 좋아요, 싫어요 상태에 따라 가중치 결정
     * */
    private int calculateMultiplier(LikeStatus previousStatus, LikeStatus currentStatus) {
        if (previousStatus == LikeStatus.LIKE && currentStatus == LikeStatus.DISLIKE) {
            return -2;  // 좋아요 상태에서 싫어요로 전환: 책의 성향 점수 * -2
        } else if (previousStatus == LikeStatus.DISLIKE && currentStatus == LikeStatus.LIKE) {
            return 2;   // 싫어요 상태에서 좋아요로 전환: 책의 성향 점수 * 2
        } else if (currentStatus == LikeStatus.LIKE) {
            return 1;   // 좋아요: 책의 성향 점수 * 1
        } else if (currentStatus == LikeStatus.DISLIKE) {
            return -1;  // 싫어요: 책의 성향 점수 * -1
        } else if (previousStatus == LikeStatus.LIKE && currentStatus == LikeStatus.CANCEL) {
            return -1;  // 좋아요 취소: 책의 성향 점수 * -1
        } else if (previousStatus == LikeStatus.DISLIKE && currentStatus == LikeStatus.CANCEL) {
            return 1;   // 싫어요 취소: 책의 성향 점수 * 1
        }
        return 0;  // 상태 변경이 없는 경우
    }
}
