package com.kidsworld.kidsping.domain.book.controller;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import com.kidsworld.kidsping.domain.book.service.BookService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody BookRequest request) {
        BookResponse response = bookService.createBook(request);
        return ApiResponse.created("/api/books/" + response.getId(),
                ExceptionCode.CREATED.getCode(),
                response,
                "도서가 성공적으로 등록되었습니다.");
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable Long bookId) {
        BookResponse response = bookService.getBook(bookId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(),
                response,
                ExceptionCode.OK.getMessage());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getAllBooks(Pageable pageable) {
        Page<BookResponse> response = bookService.getAllBooks(pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(),
                response,
                ExceptionCode.OK.getMessage());
    }

    @PutMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(bookId, request);
        return ApiResponse.ok(ExceptionCode.OK.getCode(),
                response,
                "도서가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(),
                null,
                "도서가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getBooksByGenre(
            @PathVariable Long genreId,
            Pageable pageable) {
        Page<BookResponse> response = bookService.getBooksByGenre(genreId, pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "장르별 도서 목록을 성공적으로 조회했습니다.");
    }

    @GetMapping("/top-genre")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getTopGenreBooks(Pageable pageable) {
        Page<BookResponse> response = bookService.getTopGenreBooks(pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "최다 조회 장르의 도서 목록을 성공적으로 조회했습니다.");
    }

    @GetMapping("/kid/{kidId}/combi")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getCompatibleBooks (
            @PathVariable Long kidId, Pageable pageable) {
        Page<BookResponse> response = bookService.getCompatibleBooks(kidId, pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "MBTI 궁합 도서를 성공적으로 조회했습니다.");
    }

    @GetMapping("/kid/{kidId}/genre")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getTopGenreBooksByKid(
            @PathVariable Long kidId, Pageable pageable) {
        Page<BookResponse> response = bookService.getTopGenreBooksByKid(kidId, pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "아이의 최고 선호 장르 도서 목록을 성공적으로 조회했습니다.");
    }

    @GetMapping("/kid/{kidId}/mbti")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getRecommendedBooks(
            @PathVariable Long kidId, Pageable pageable) {
        Page<BookResponse> response = bookService.getRecommendedBooks(kidId, pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "아이 성향에 맞는 도서를 성공적으로 조회했습니다");
    }
}
