package com.kidsworld.kidsping.domain.question.entity;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
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
public class GenreAnswer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Kid kid;

    @Column(name= "is_deleted")
    private Boolean isDeleted;

    @Builder
    public GenreAnswer(Kid kid, Boolean isDeleted) {
        this.kid = kid;
        this.isDeleted = isDeleted;
    }
}