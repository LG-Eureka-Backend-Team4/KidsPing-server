package com.kidsworld.kidsping.domain.genre.entity;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenreScore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id", nullable = false)
    private Kid kid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Range(min = 0, max = 100)
    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Builder
    public GenreScore(Kid kid, Genre genre, Integer score, Boolean isDeleted) {
        this.kid = kid;
        this.genre = genre;
        this.score = score;
        this.isDeleted = isDeleted;
    }

    public void updateScore(int additionalScore) {
        this.score += additionalScore;
    }
}
