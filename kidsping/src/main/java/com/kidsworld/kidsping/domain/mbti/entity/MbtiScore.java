package com.kidsworld.kidsping.domain.mbti.entity;

import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiScore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_score_id")
    private Long id;

    private int extroversionScore;
    private int introversionScore;
    private int sensingScore;
    private int intuitionScore;
    private int thinkingScore;
    private int feelingScore;
    private int judgingScore;
    private int perceivingScore;

    private boolean isDeleted;
}