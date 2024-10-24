package com.kidsworld.kidsping.domain.like.repository;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeMbtiRepository extends JpaRepository<LikeMbti, Long> {

    Optional<LikeMbti> findByKidIdAndBookId(Long kidId, Long bookId);

    Optional<LikeMbti> findLikeMbtiByKidAndBook(Kid kid, Book book);

    @Query("select lb from LikeMbti lb "
            + "JOIN FETCH lb.book b "
            + "JOIN FETCH b.bookMbti "
            + "where lb.kid.id = :kidId and b.isDeleted = false")
    Page<LikeMbti> findBooksByLikeMbtiId(@Param("kidId") Long kidId, Pageable pageable);
}