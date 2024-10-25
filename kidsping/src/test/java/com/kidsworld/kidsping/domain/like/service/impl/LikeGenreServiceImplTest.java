package com.kidsworld.kidsping.domain.like.service.impl;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.entity.LikeGenre;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeGenreRepository;
import com.kidsworld.kidsping.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeGenreServiceImplTest {

    @Mock
    private LikeGenreRepository likeGenreRepository;

    @Mock
    private KidRepository kidRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreScoreService genreScoreService;

    @InjectMocks
    private LikeGenreServiceImpl likeGenreService;

    private Kid kid;
    private Book book;

    @BeforeEach
    void setUp() {
        kid = Kid.builder()
                .id(1L)
                .build();

        book = Book.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("새로 좋아요를 추가할 때 LikeGenre가 생성되고 점수가 업데이트되는지 확인")
    void testLike() {
        LikeGenre newLike = LikeGenre.builder()
                .kid(kid)
                .book(book)
                .likeStatus(LikeStatus.LIKE)
                .previousLikeStatus(null)
                .build();

        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())).thenReturn(Optional.empty());

        when(likeGenreRepository.save(any(LikeGenre.class))).thenReturn(newLike);

        likeGenreService.like(kid.getId(), book.getId());

        verify(genreScoreService).updateScore(kid, book.getGenre(), null, LikeStatus.LIKE);
        verify(likeGenreRepository).save(any(LikeGenre.class));
    }

    @Test
    @DisplayName("좋아요를 취소할 때 점수 업데이트 및 LikeGenre 삭제 여부 확인")
    void testLikeCancel() {
        LikeGenre existingLike = LikeGenre.builder()
                .likeStatus(LikeStatus.LIKE)
                .kid(kid)
                .book(book)
                .build();

        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())).thenReturn(Optional.of(existingLike));

        likeGenreService.likeCancel(kid.getId(), book.getId());

        verify(genreScoreService).updateScore(kid, book.getGenre(), LikeStatus.LIKE, LikeStatus.CANCEL);
        verify(likeGenreRepository).delete(existingLike);
    }

    @Test
    @DisplayName("새로 싫어요를 추가할 때 LikeGenre가 생성되고 점수가 업데이트되는지 확인")
    void testDislike() {
        LikeGenre newDislike = LikeGenre.builder()
                .kid(kid)
                .book(book)
                .likeStatus(LikeStatus.DISLIKE)
                .previousLikeStatus(null)
                .build();

        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())).thenReturn(Optional.empty());

        when(likeGenreRepository.save(any(LikeGenre.class))).thenReturn(newDislike);

        likeGenreService.dislike(kid.getId(), book.getId());

        verify(genreScoreService).updateScore(kid, book.getGenre(), null, LikeStatus.DISLIKE);
        verify(likeGenreRepository).save(any(LikeGenre.class));
    }

    @Test
    @DisplayName("싫어요를 취소할 때 점수 업데이트 및 LikeGenre 삭제 여부 확인")
    void testDislikeCancel() {
        LikeGenre existingDislike = LikeGenre.builder()
                .likeStatus(LikeStatus.DISLIKE)
                .kid(kid)
                .book(book)
                .build();

        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())).thenReturn(Optional.of(existingDislike));

        likeGenreService.dislikeCancel(kid.getId(), book.getId());

        verify(genreScoreService).updateScore(kid, book.getGenre(), LikeStatus.DISLIKE, LikeStatus.CANCEL);
        verify(likeGenreRepository).delete(existingDislike);
    }

    @Test
    @DisplayName("이미 좋아요 상태에서 다시 좋아요 요청 시 예외 발생")
    void testLikeAlreadyLiked() {
        LikeGenre existingLike = LikeGenre.builder()
                .likeStatus(LikeStatus.LIKE)
                .kid(kid)
                .book(book)
                .build();

        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())).thenReturn(Optional.of(existingLike));

        assertThrows(GlobalException.class, () -> likeGenreService.like(kid.getId(), book.getId()));
    }

    @Test
    @DisplayName("이미 싫어요 상태에서 다시 싫어요 요청 시 예외 발생")
    void testDislikeAlreadyDisliked() {
        LikeGenre existingDislike = LikeGenre.builder()
                .likeStatus(LikeStatus.DISLIKE)
                .kid(kid)
                .book(book)
                .build();

        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likeGenreRepository.findByKidIdAndBookId(kid.getId(), book.getId())).thenReturn(Optional.of(existingDislike));

        assertThrows(GlobalException.class, () -> likeGenreService.dislike(kid.getId(), book.getId()));
    }
}
