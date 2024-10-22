package com.kidsworld.kidsping.domain.book.service;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequestDto;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto createBook(BookRequestDto bookRequestDto);
    BookResponseDto getBook(Long id);
    Page<BookResponseDto> getAllBooks(Pageable pageable);
    BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto);
    void deleteBook(Long id);
}
