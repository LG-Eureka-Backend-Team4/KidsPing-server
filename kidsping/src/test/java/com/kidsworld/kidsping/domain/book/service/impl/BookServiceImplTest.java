package com.kidsworld.kidsping.domain.book.service.impl;

import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.BookMbti;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import com.kidsworld.kidsping.domain.book.repository.BookMbtiRepository;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;
import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.domain.genre.repository.GenreScoreRepository;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.global.common.entity.CommonCode;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.common.repository.CommonCodeRepository;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookMbtiRepository bookMbtiRepository;

    @Mock
    private GenreScoreRepository genreScoreRepository;

    @Mock
    private Genre genre;

    @Mock
    private KidRepository kidRepository;

    @Mock
    private CommonCodeRepository commonCodeRepository;

    @Mock
    private Kid kid;

    @Mock
    private KidMbti kidMbti;

    @Mock
    private CommonCode commonCode;

    @Mock
    private GenreScoreService genreScoreService;

    private BookMbti bookMbti;
    private Book book;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {

        bookMbti = BookMbti.builder()
                .bookMbtiType(MbtiType.ENFP)
                .eScore(70)
                .iScore(30)
                .sScore(40)
                .nScore(60)
                .tScore(45)
                .fScore(55)
                .jScore(35)
                .pScore(65)
                .isDeleted(false)
                .build();
        ReflectionTestUtils.setField(bookMbti, "id", 1L);

        book = Book.builder()
                .id(1L)
                .bookMbti(bookMbti)
                .genre(genre)
                .title("테스트 책")
                .summary("테스트 요약")
                .author("테스트 작가")
                .publisher("테스트 출판사")
                .age(5)
                .imageUrl("https://test-image.com")
                .isDeleted(false)
                .build();

        bookRequest = BookRequest.builder()
                .genreId(1L)
                .title("테스트 책")
                .summary("테스트 요약")
                .author("테스트 작가")
                .publisher("테스트 출판사")
                .age(5)
                .imageUrl("https://test-image.com")
                .bookMbtiType(MbtiType.ENFP)
                .eScore(70)
                .iScore(30)
                .sScore(40)
                .nScore(60)
                .tScore(45)
                .fScore(55)
                .jScore(35)
                .pScore(65)
                .build();
    }

    @Test
    @DisplayName("도서 생성에 성공한다")
    void createBook_Success() {
        // Given
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        given(bookMbtiRepository.save(any(BookMbti.class))).willReturn(bookMbti);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // When
        BookResponse response = bookService.createBook(bookRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("테스트 책");
        assertThat(response.getMbtiType()).isEqualTo(MbtiType.ENFP);
        verify(bookMbtiRepository).save(any(BookMbti.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("도서 조회에 성공한다")
    void getBook_Success() {
        // Given
        given(bookRepository.findBookWithMbtiByBookId(1L))
                .willReturn(Optional.of(book));

        // When
        BookResponse response = bookService.getBook(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("테스트 책");
        assertThat(response.getMbtiType()).isEqualTo(MbtiType.ENFP);
    }

    @Test
    @DisplayName("존재하지 않는 도서 조회 시 예외가 발생한다")
    void getBook_NotFound_ThrowsException() {
        // Given
        given(bookRepository.findBookWithMbtiByBookId(999L))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.getBook(999L))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_BOOK);
    }

    @Test
    @DisplayName("모든 도서 목록을 조회한다")
    void getAllBooks_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        given(bookRepository.findAll(pageable)).willReturn(bookPage);

        // When
        Page<BookResponse> responses = bookService.getAllBooks(pageable);

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses.getContent()).hasSize(1);
        assertThat(responses.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("장르별 도서 목록을 조회한다")
    void getBooksByGenre_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        given(genreRepository.existsById(1L)).willReturn(true);
        given(bookRepository.findBookByGenreId(1L, pageable)).willReturn(bookPage);
        given(genre.getId()).willReturn(1L);

        // When
        Page<BookResponse> responses = bookService.getBooksByGenre(1L, pageable);

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses.getContent()).hasSize(1);
        BookResponse response = responses.getContent().get(0);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getGenreId()).isEqualTo(1L);
                    assertThat(r.getTitle()).isEqualTo("테스트 책");
                    assertThat(r.getMbtiType()).isEqualTo(MbtiType.ENFP);
                });

        verify(genreRepository).existsById(1L);
        verify(bookRepository).findBookByGenreId(1L, pageable);
    }

    @Test
    @DisplayName("도서 수정에 성공한다")
    void updateBook_Success() {
        // Given
        given(bookRepository.findBookWithMbtiByBookId(1L)).willReturn(Optional.of(book));
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        given(bookMbtiRepository.save(any(BookMbti.class))).willReturn(bookMbti);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // When
        BookResponse response = bookService.updateBook(1L, bookRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("테스트 책");
    }

    @Test
    @DisplayName("도서 삭제에 성공한다")
    void deleteBook_Success() {
        // Given
        given(bookRepository.findBookWithMbtiByBookId(1L))
                .willReturn(Optional.of(book));

        // When
        bookService.deleteBook(1L);

        // Then
        assertThat(book.getIsDeleted()).isTrue();
        assertThat(book.getBookMbti().getIsDeleted()).isTrue();
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("아이의 MBTI와 궁합이 맞는 도서 목록을 조회한다")
    void getCompatibleBooks_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        given(kidRepository.findKidWithMbtiByKidId(1L)).willReturn(Optional.of(kid));
        given(kid.getKidMbti()).willReturn(kidMbti);
        given(kidMbti.getMbtiStatus()).willReturn(MbtiStatus.ENFP);
        given(commonCodeRepository.findByGroupCodeAndCommonCode("MCP", "ENFP"))
                .willReturn(Optional.of(commonCode));
        given(commonCode.getCodeName()).willReturn("INTJ");
        given(bookRepository.findCompatibleBooks(MbtiType.INTJ, pageable)).willReturn(bookPage);

        // When
        Page<BookResponse> response = bookService.getCompatibleBooks(1L, pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        BookResponse bookResponse = response.getContent().get(0);
        assertThat(bookResponse.getId()).isEqualTo(1L);
        assertThat(bookResponse.getTitle()).isEqualTo("테스트 책");

        verify(kidRepository).findKidWithMbtiByKidId(1L);
        verify(commonCodeRepository).findByGroupCodeAndCommonCode("MCP", "ENFP");
        verify(bookRepository).findCompatibleBooks(MbtiType.INTJ, pageable);
    }

    @Test
    @DisplayName("존재하지 않는 아이의 MBTI 궁합 도서 조회 시 예외가 발생한다")
    void getCompatibleBooks_KidNotFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        given(kidRepository.findKidWithMbtiByKidId(999L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.getCompatibleBooks(999L, pageable))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID);
    }

    @Test
    @DisplayName("아이의 최고 선호 장르의 도서 목록을 조회한다")
    void getTopGenreBooksByKid_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        TopGenreResponse topGenreResponse = TopGenreResponse.builder()
                .genreId(1L)
                .genreTitle("테스트 장르")
                .score(100)
                .build();

        given(genre.getId()).willReturn(1L);
        given(genreScoreService.getTopGenre(1L)).willReturn(topGenreResponse);
        given(genreRepository.existsById(1L)).willReturn(true);
        given(bookRepository.findBookByGenreId(1L, pageable)).willReturn(bookPage);

        // When
        Page<BookResponse> response = bookService.getTopGenreBooksByKid(1L, pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        BookResponse bookResponse = response.getContent().get(0);
        assertThat(bookResponse)
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getGenreId()).isEqualTo(1L);
                    assertThat(r.getTitle()).isEqualTo("테스트 책");
                    assertThat(r.getMbtiType()).isEqualTo(MbtiType.ENFP);
                });

        verify(genreScoreService).getTopGenre(1L);
        verify(genreRepository).existsById(1L);
        verify(bookRepository).findBookByGenreId(1L, pageable);
    }

    @Test
    @DisplayName("존재하지 않는 아이의 최고 선호 장르 도서 조회 시 예외가 발생한다")
    void getTopGenreBooksByKid_KidNotFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        given(genreScoreService.getTopGenre(999L))
                .willThrow(new GlobalException(ExceptionCode.NOT_FOUND_KID));

        // When & Then
        assertThatThrownBy(() -> bookService.getTopGenreBooksByKid(999L, pageable))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID);

        verify(genreScoreService).getTopGenre(999L);
    }

    @Test
    @DisplayName("아이의 MBTI에 맞는 도서 목록을 조회한다")
    void getRecommendedBooks_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        given(kidRepository.findKidWithMbtiByKidId(1L)).willReturn(Optional.of(kid));
        given(kid.getKidMbti()).willReturn(kidMbti);
        given(kidMbti.getMbtiStatus()).willReturn(MbtiStatus.ENFP);
        given(bookRepository.findBooksByMbtiType(MbtiType.ENFP, pageable)).willReturn(bookPage);

        // When
        Page<BookResponse> response = bookService.getRecommendedBooks(1L, pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        BookResponse bookResponse = response.getContent().get(0);
        assertThat(bookResponse)
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getTitle()).isEqualTo("테스트 책");
                    assertThat(r.getMbtiType()).isEqualTo(MbtiType.ENFP);
                });

        verify(kidRepository).findKidWithMbtiByKidId(1L);
        verify(bookRepository).findBooksByMbtiType(MbtiType.ENFP, pageable);
    }

    @Test
    @DisplayName("존재하지 않는 아이의 MBTI 도서 조회 시 예외가 발생한다")
    void getRecommendedBooks_KidNotFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        given(kidRepository.findKidWithMbtiByKidId(999L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.getRecommendedBooks(999L, pageable))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID);

        verify(kidRepository).findKidWithMbtiByKidId(999L);
    }

    @Test
    @DisplayName("MBTI 정보가 없는 아이의 도서 추천 조회 시 예외가 발생한다")
    void getRecommendedBooks_MbtiNotFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        given(kidRepository.findKidWithMbtiByKidId(1L)).willReturn(Optional.of(kid));
        given(kid.getKidMbti()).willReturn(null);

        // When & Then
        assertThatThrownBy(() -> bookService.getRecommendedBooks(1L, pageable))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID_MBTI);

        verify(kidRepository).findKidWithMbtiByKidId(1L);
    }
}