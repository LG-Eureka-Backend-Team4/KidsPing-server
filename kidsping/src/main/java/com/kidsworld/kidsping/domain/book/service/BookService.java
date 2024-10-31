package com.kidsworld.kidsping.domain.book.service;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);
    BookResponse getBook(Long bookId);
    Page<BookResponse> getAllBooks(Pageable pageable);
    BookResponse updateBook(Long bookId, BookRequest bookRequest);
    void deleteBook(Long bookId);
    Page<BookResponse> getBooksByGenre(Long genreId, Pageable pageable);
    Page<BookResponse> getCompatibleBooks(Long kidId, Pageable pageable);
    Page<BookResponse> getTopGenreBooksByKid(Long kidId, Pageable pageable);
    Page<BookResponse> getRecommendedBooks(Long kidId, Pageable pageable);
}
