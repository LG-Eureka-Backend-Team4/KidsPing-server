package com.kidsworld.kidsping.domain.like.entity;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
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
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes_mbti")
public class LikeMbti extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_mbti_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Kid kid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Transient
    private LikeStatus previousLikeStatus;

    @Builder
    public LikeMbti(LikeStatus likeStatus, Kid kid, Book book, LikeStatus previousLikeStatus) {
        this.likeStatus = likeStatus;
        this.kid = kid;
        this.book = book;
        this.previousLikeStatus = previousLikeStatus;
    }

    public void changeLikeStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }

    public void savePreviousLikeStatus(LikeStatus previousLikeStatus) {
        this.previousLikeStatus = previousLikeStatus;
    }
}