package com.kidsworld.kidsping.domain.kid.entity;

import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import com.kidsworld.kidsping.global.common.entity.MbtiScore;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "kid_mbti")
public class KidMbti extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kid_mbti_id")
    private Long id;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "e_score")
    private Integer eScore;

    @Column(name = "i_score")
    private Integer iScore;

    @Column(name = "s_score")
    private Integer sScore;

    @Column(name = "n_score")
    private Integer nScore;

    @Column(name = "t_score")
    private Integer tScore;

    @Column(name = "f_score")
    private Integer fScore;

    @Column(name = "j_score")
    private Integer jScore;

    @Column(name = "p_score")
    private Integer pScore;

    @Enumerated(EnumType.STRING)
    private MbtiStatus mbtiStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Kid kid;

    public void updateKid(Kid kid) {
        this.kid = kid;
    }

    @Builder
    public KidMbti(Integer eScore, Integer iScore, Integer sScore, Integer nScore, Integer tScore, Integer fScore,
                   Integer jScore, Integer pScore, MbtiStatus mbtiStatus) {
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

    public void updateMbti(MbtiScore mbtiScore, MbtiStatus mbtiStatus) {
        this.eScore = mbtiScore.getEScore();
        this.iScore = mbtiScore.getIScore();
        this.sScore = mbtiScore.getSScore();
        this.nScore = mbtiScore.getNScore();
        this.tScore = mbtiScore.getTScore();
        this.fScore = mbtiScore.getFScore();
        this.jScore = mbtiScore.getJScore();
        this.pScore = mbtiScore.getPScore();
        this.mbtiStatus = mbtiStatus;
    }
}