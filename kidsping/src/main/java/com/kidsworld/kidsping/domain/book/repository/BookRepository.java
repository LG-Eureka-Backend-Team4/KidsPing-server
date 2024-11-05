package com.kidsworld.kidsping.domain.book.repository;

import com.kidsworld.kidsping.domain.book.entity.Book;
import java.util.Optional;

import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join fetch b.bookMbti where b.id = :bookId")
    Optional<Book> findBookWithMbtiByBookId(@Param("bookId") Long bookId);

    @Override
    @Query(value = "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookMbti " +
            "LEFT JOIN FETCH b.genre " +
            "WHERE b.isDeleted = false",
            countQuery = "SELECT COUNT(b) FROM Book b WHERE b.isDeleted = false")
    Page<Book> findAll(Pageable pageable);

    @Query(value = "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookMbti " +
            "LEFT JOIN FETCH b.genre g " +
            "WHERE g.id = :genreId " +
            "AND b.isDeleted = false " +
            "AND g.isDeleted = false",
            countQuery = "SELECT COUNT(b) FROM Book b " +
                    "LEFT JOIN b.genre g " +
                    "WHERE g.id = :genreId " +
                    "AND b.isDeleted = false " +
                    "AND g.isDeleted = false")
    Page<Book> findBookByGenreId(@Param("genreId") Long genreId, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "JOIN FETCH b.bookMbti bm " +
            "WHERE bm.bookMbtiType = :mbtiType " +
            "AND b.isDeleted = false")
    Page<Book> findCompatibleBooks(
            @Param("mbtiType") MbtiType mbtiType,
            Pageable pageable
    );

    @Query(value = "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookMbti bm " +
            "LEFT JOIN FETCH b.genre " +
            "WHERE bm.bookMbtiType = :mbtiType " +
            "AND b.isDeleted = false",
            countQuery = "SELECT COUNT(b) FROM Book b " +
                    "LEFT JOIN b.bookMbti bm " +
                    "WHERE bm.bookMbtiType = :mbtiType " +
                    "AND b.isDeleted = false")
    Page<Book> findBooksByMbtiType(@Param("mbtiType") MbtiType mbtiType, Pageable pageable);
}
