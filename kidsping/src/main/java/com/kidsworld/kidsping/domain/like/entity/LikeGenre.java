package com.kidsworld.kidsping.domain.like.entity;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeGenre extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_genre_id")
    private Long id;

    @Column(name= "is_deleted")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id")
    private Kid kid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
