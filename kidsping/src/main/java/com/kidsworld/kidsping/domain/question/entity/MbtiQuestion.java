package com.kidsworld.kidsping.domain.question.entity;

import com.kidsworld.kidsping.domain.mbti.entity.enums.PersonalityTrait;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_question_id")
    private Long id;

    @Column(name = "content", length = 50)
    private String content;

    @Enumerated(EnumType.STRING)
    private PersonalityTrait personalityTrait;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}