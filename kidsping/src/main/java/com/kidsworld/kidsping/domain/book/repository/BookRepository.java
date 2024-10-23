package com.kidsworld.kidsping.domain.book.repository;

import com.kidsworld.kidsping.domain.book.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join fetch b.bookMbti where b.id = :bookId")
    Optional<Book> findBookWithMbti(@Param("bookId") Long bookId);
}
