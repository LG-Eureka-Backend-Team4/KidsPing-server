package com.kidsworld.kidsping.domain.question.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiQuestion extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_question_id")
    private Long Id;

    @Column(name = "mbti_question_content",length = 50)
    private String content;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}