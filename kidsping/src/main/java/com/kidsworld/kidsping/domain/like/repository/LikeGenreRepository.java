package com.kidsworld.kidsping.domain.like.repository;

import com.kidsworld.kidsping.domain.like.entity.LikeGenre;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeGenreRepository extends JpaRepository<LikeGenre, Long> {

    Optional<LikeGenre> findByKidIdAndBookId(Long kidId, Long bookId);

    void deleteByKidId(Long kidId);

    @Query("SELECT COUNT(lg) FROM LikeGenre lg WHERE lg.kid.id = :kidId AND lg.likeStatus = :status")
    int countLikesByKidId(@Param("kidId") Long kidId, @Param("status") LikeStatus status);
}
