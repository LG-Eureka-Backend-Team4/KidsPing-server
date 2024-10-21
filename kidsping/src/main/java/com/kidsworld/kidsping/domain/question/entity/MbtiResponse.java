package com.kidsworld.kidsping.domain.question.entity;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiResponse extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_response_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Kid kid;

    private int eScore;
    private int iScore;
    private int sScore;
    private int nScore;
    private int tScore;
    private int fScore;
    private int jScore;
    private int pScore;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}