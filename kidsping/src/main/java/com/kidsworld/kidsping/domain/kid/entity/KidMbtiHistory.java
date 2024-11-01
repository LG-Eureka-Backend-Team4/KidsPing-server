package com.kidsworld.kidsping.domain.kid.entity;

import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "kid_mbti_history")
public class KidMbtiHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kid_mbti_history_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MbtiStatus mbtiStatus;

    private boolean isDeleted =Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Kid kid;

    @Builder
    public KidMbtiHistory(MbtiStatus mbtiStatus, boolean isDeleted, Kid kid) {
        this.mbtiStatus = mbtiStatus;
        this.isDeleted = isDeleted;
        this.kid = kid;
    }
}