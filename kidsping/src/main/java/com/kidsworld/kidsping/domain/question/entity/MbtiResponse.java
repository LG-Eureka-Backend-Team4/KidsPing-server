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
@NoArgsConstructor
public class MbtiResponse extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_response_id")
    private Long mbtiResponseId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Long kidId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_question_id")
    private Long mbtiQuestionId;

    @Column(length = 10) //보류
    private String content;

    private Integer score;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}