package com.kidsworld.kidsping.domain.book.controller;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequestDto;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponseDto;
import com.kidsworld.kidsping.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto createdBook = bookService.createBook(bookRequestDto);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        BookResponseDto book = bookService.getBook(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(Pageable pageable) {
        Page<BookResponseDto> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto updatedBook = bookService.updateBook(id, bookRequestDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
