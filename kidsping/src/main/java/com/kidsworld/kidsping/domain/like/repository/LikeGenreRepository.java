package com.kidsworld.kidsping.domain.like.repository;

import com.kidsworld.kidsping.domain.like.entity.LikeGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeGenreRepository extends JpaRepository<LikeGenre, Long> {

    Optional<LikeGenre> findByKidIdAndBookId(Long kidId, Long bookId);

    void deleteByKidId(Long kidId);
}
