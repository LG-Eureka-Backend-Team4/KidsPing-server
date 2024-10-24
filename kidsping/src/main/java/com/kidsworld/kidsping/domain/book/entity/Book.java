package com.kidsworld.kidsping.domain.book.entity;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Book extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "mbti_id", nullable = false)
    private BookMbti bookMbti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "author", length = 50, nullable = false)
    private String author;

    @Column(name = "publisher", length = 50)
    private String publisher;

    @Column(name = "age")
    private Integer age;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "image_url")
    private String imageUrl;

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}