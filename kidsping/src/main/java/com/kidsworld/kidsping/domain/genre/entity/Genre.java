package com.kidsworld.kidsping.domain.genre.entity;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name= "is_deleted")
    private Boolean isDeleted;

    @OneToOne(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GenreFile genreFile;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenreScore> genreScores = new ArrayList<>();

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
}
