package com.kidsworld.kidsping.domain.question.entity;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_question_id")
    private MbtiQuestion mbtiQuestion;

    @Column(length = 10) //보류
    private String content;

    private Integer score;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}