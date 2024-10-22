package com.kidsworld.kidsping.domain.like.repository;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeMbtiRepository extends JpaRepository<LikeMbti, Long> {

    Optional<LikeMbti> findByKidIdAndBookId(Long kidId, Long bookId);

    Optional<LikeMbti> findLikeMbtiByKidAndBook(Kid kid, Book book);
}