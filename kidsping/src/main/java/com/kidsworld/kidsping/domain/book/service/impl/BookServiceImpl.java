package com.kidsworld.kidsping.domain.book.service.impl;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.BookMbti;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import com.kidsworld.kidsping.domain.book.repository.BookMbtiRepository;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.book.service.BookService;
import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;
import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.domain.genre.repository.GenreScoreRepository;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.global.common.entity.CommonCode;
import com.kidsworld.kidsping.global.common.repository.CommonCodeRepository;
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
    private final GenreScoreRepository genreScoreRepository;
    private final KidRepository kidRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final GenreScoreService genreScoreService;

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
    public BookResponse getBook(Long bookId) {
        Book book = bookRepository.findBookWithMbtiByBookId(bookId)
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
    public BookResponse updateBook(Long bookId, BookRequest bookRequest) {
        Book book = bookRepository.findBookWithMbtiByBookId(bookId)
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
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findBookWithMbtiByBookId(bookId)
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

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getTopGenreBooks(Pageable pageable) {
        Genre topGenre = genreScoreRepository.findTopGenre()
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE));

        Page<Book> books = bookRepository.findBookByGenreId(topGenre.getId(), pageable);

        return books.map(BookResponse::from);
    }

    @Override
    public Page<BookResponse> getCompatibleBooks(Long kidId, Pageable pageable) {
        Kid kid = kidRepository.findKidWithMbtiByKidId(kidId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_KID));

        if (kid.getKidMbti() == null) {
            throw new GlobalException(ExceptionCode.NOT_FOUND_KID_MBTI);
        }

        String kidMbtiType = kid.getKidMbti().getMbtiStatus().name();
        CommonCode compatibilityCode = commonCodeRepository.findByGroupCodeAndCommonCode("MCP", kidMbtiType)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_COMPATIBILITY));

        String compatibleMbtiStr = compatibilityCode.getCodeName();
        MbtiType compatibleType = MbtiType.valueOf(compatibleMbtiStr);

        return bookRepository.findCompatibleBooks(compatibleType, pageable)
                .map(BookResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getTopGenreBooksByKid(Long kidId, Pageable pageable) {
        TopGenreResponse topGenre = genreScoreService.getTopGenre(kidId);
        return getBooksByGenre(topGenre.getGenreId(), pageable);
    }
}
