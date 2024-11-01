package com.kidsworld.kidsping.domain.like.repository;

import com.kidsworld.kidsping.domain.like.entity.LikeGenre;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeGenreRepository extends JpaRepository<LikeGenre, Long> {

    Optional<LikeGenre> findByKidIdAndBookId(Long kidId, Long bookId);

    @Modifying
    @Query("delete from LikeGenre lg where lg.kid.id = :kidId")
    void deleteLikeGenresByKidId(@Param("kidId") Long kidId);

    @Query("SELECT COUNT(lg) FROM LikeGenre lg WHERE lg.kid.id = :kidId AND lg.likeStatus = :status")
    int countLikesByKidId(@Param("kidId") Long kidId, @Param("status") LikeStatus status);
}
