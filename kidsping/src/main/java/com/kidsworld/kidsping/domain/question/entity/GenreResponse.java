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
public class GenreResponse extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_response_id")
    private Long genreResponseId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_question_id")
    private Long genreQuestionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Long kidId;

    @Column(length = 10)
    private String content;

    private Integer score;

    @Column(name= "is_deleted")
    private Boolean isDeleted;
}