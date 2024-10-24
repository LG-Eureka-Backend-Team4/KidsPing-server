package com.kidsworld.kidsping.domain.genre.entity;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToOne(mappedBy = "genre", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private GenreFile genreFile;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private Set<GenreScore> genreScores = new HashSet<>();

    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private Set<Book> books = new HashSet<>();
}
