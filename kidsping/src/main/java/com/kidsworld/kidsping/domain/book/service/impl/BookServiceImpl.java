package com.kidsworld.kidsping.domain.book.service.impl;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.BookMbti;
import com.kidsworld.kidsping.domain.book.repository.BookMbtiRepository;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.book.service.BookService;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
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
    private final BookMbtiRepository bookMbtiRepository;

    @Override
    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        BookMbti bookMbti = BookMbti.builder()
                .bookMbtiType(bookRequest.getBookMbtiType())
                .eScore(bookRequest.getEScore())
                .iScore(bookRequest.getIScore())
                .sScore(bookRequest.getSScore())
                .nScore(bookRequest.getNScore())
                .tScore(bookRequest.getTScore())
                .fScore(bookRequest.getFScore())
                .jScore(bookRequest.getJScore())
                .pScore(bookRequest.getPScore())
                .isDeleted(false)
                .build();

        BookMbti savedBookMbti = bookMbtiRepository.save(bookMbti);

        Book book = Book.builder()
                .genre(genreRepository.findById(bookRequest.getGenreId())
                        .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE)))
                .bookMbti(savedBookMbti)
                .title(bookRequest.getTitle())
                .summary(bookRequest.getSummary())
                .author(bookRequest.getAuthor())
                .publisher(bookRequest.getPublisher())
                .age(bookRequest.getAge())
                .imageUrl(bookRequest.getImageUrl())
                .isDeleted(false)
                .build();

        Book savedBook = bookRepository.save(book);
        return BookResponse.from(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBook(Long id) {
        Book book = bookRepository.findBookWithMbtiByBookId(id)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_BOOK));

        if (book.getIsDeleted()) {
            throw new GlobalException(ExceptionCode.NOT_FOUND_BOOK);
        }

        return BookResponse.from(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(BookResponse::from);
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findBookWithMbtiByBookId(id)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_BOOK));

        BookMbti updatedBookMbti = BookMbti.builder()
                .id(book.getBookMbti().getId())
                .bookMbtiType(bookRequest.getBookMbtiType())
                .eScore(bookRequest.getEScore())
                .iScore(bookRequest.getIScore())
                .sScore(bookRequest.getSScore())
                .nScore(bookRequest.getNScore())
                .tScore(bookRequest.getTScore())
                .fScore(bookRequest.getFScore())
                .jScore(bookRequest.getJScore())
                .pScore(bookRequest.getPScore())
                .isDeleted(false)
                .build();

        BookMbti savedBookMbti = bookMbtiRepository.save(updatedBookMbti);

        Book updatedBook = Book.builder()
                .id(book.getId())
                .genre(genreRepository.findById(bookRequest.getGenreId())
                        .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE)))
                .bookMbti(savedBookMbti)
                .title(bookRequest.getTitle())
                .summary(bookRequest.getSummary())
                .author(bookRequest.getAuthor())
                .publisher(bookRequest.getPublisher())
                .age(bookRequest.getAge())
                .imageUrl(bookRequest.getImageUrl())
                .isDeleted(false)
                .build();

        Book savedBook = bookRepository.save(updatedBook);
        return BookResponse.from(savedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findBookWithMbtiByBookId(id)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_BOOK));
        book.setIsDeleted(true);
        book.getBookMbti().setIsDeleted(true);
        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getBooksByGenre(Long genreId, Pageable pageable) {
        if (!genreRepository.existsById(genreId)) {
            throw new GlobalException(ExceptionCode.NOT_FOUND_GENRE);
        }

        Page<Book> books = bookRepository.findBookByGenreId(genreId, pageable);
        return books.map(BookResponse::from);
    }
}
