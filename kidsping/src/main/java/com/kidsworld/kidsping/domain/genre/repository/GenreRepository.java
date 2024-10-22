package com.kidsworld.kidsping.domain.genre.repository;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
