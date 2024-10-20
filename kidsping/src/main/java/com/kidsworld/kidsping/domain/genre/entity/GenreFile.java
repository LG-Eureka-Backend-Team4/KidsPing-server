package com.kidsworld.kidsping.domain.genre.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenreFile {

    @Id
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "random_value")
    private String randomValue;

    @OneToOne
    @JoinColumn(name = "file_id")
    private Genre genre;
}
