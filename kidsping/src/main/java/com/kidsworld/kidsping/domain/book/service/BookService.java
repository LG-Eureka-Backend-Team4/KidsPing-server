package com.kidsworld.kidsping.domain.book.service;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);
    BookResponse getBook(Long id);
    Page<BookResponse> getAllBooks(Pageable pageable);
    BookResponse updateBook(Long id, BookRequest bookRequest);
    void deleteBook(Long id);
    Page<BookResponse> getBooksByGenre(Long genreId, Pageable pageable);
    Page<BookResponse> getTopGenreBooks(Pageable pageable);
}
