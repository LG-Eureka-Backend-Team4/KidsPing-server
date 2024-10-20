package com.kidsworld.kidsping.domain.kid.entity;

import com.kidsworld.kidsping.domain.kid.entity.enums.MbtiStatus;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiScore;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KidMbti extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kid_mbti_id")
    private Long id;

    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private MbtiStatus mbtiStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_score_id")
    private MbtiScore mbtiScore;
}