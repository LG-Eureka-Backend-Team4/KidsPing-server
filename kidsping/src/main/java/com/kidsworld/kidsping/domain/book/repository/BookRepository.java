package com.kidsworld.kidsping.domain.book.repository;

import com.kidsworld.kidsping.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
