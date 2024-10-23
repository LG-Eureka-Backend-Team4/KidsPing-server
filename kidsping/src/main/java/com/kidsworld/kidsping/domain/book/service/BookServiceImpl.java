package com.kidsworld.kidsping.domain.book.service;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponseDto;
import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public BookResponseDto createBook(BookRequest bookRequest) {
        Book book = Book.builder()
                .genre(genreRepository.findById(bookRequest.getGenreId())
                        .orElseThrow(() -> new RuntimeException("Genre not found")))
                .bookMbti(null)
                .title(bookRequest.getTitle())
                .summary(bookRequest.getSummary())
                .author(bookRequest.getAuthor())
                .publisher(bookRequest.getPublisher())
                .age(bookRequest.getAge())
                .imageUrl(bookRequest.getImageUrl())
                .isDeleted(false)
                .build();
        Book savedBook = bookRepository.save(book);
        return BookResponseDto.from(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return BookResponseDto.from(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookResponseDto::from);
    }

    @Override
    @Transactional
    public BookResponseDto updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.getGenre().getId();
        Book updatedBook = Book.builder()
                .id(book.getId())
                .genre(genreRepository.findById(bookRequest.getGenreId())
                        .orElseThrow(() -> new RuntimeException("Genre not found")))
                .title(bookRequest.getTitle())
                .summary(bookRequest.getSummary())
                .author(bookRequest.getAuthor())
                .publisher(bookRequest.getPublisher())
                .age(bookRequest.getAge())
                .imageUrl(bookRequest.getImageUrl())
                .isDeleted(book.getIsDeleted())
                .build();

        Book savedBook = bookRepository.save(updatedBook);
        return BookResponseDto.from(savedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setIsDeleted(true);
        bookRepository.save(book);
    }
}
