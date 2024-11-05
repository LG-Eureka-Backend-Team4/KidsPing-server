package com.kidsworld.kidsping.domain.genre.service.impl;

import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;
import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.entity.GenreScore;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.domain.genre.repository.GenreScoreRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreScoreServiceImplTest {

    @InjectMocks
    private GenreScoreServiceImpl genreScoreService;

    @Mock
    private GenreScoreRepository genreScoreRepository;

    @Mock
    private KidRepository kidRepository;

    @Mock
    private GenreRepository genreRepository;

    private Kid kid;
    private Genre genre1;
    private Genre genre2;
    private Genre genre3;
    private GenreScore genreScore1;
    private GenreScore genreScore2;
    private GenreScore genreScore3;

    @BeforeEach
    void setUp() {
        try {
            Constructor<Kid> kidConstructor = Kid.class.getDeclaredConstructor();
            kidConstructor.setAccessible(true);
            kid = kidConstructor.newInstance();
            ReflectionTestUtils.setField(kid, "id", 1L);

            Constructor<Genre> genreConstructor = Genre.class.getDeclaredConstructor();
            genreConstructor.setAccessible(true);

            genre1 = genreConstructor.newInstance();
            ReflectionTestUtils.setField(genre1, "id", 1L);
            ReflectionTestUtils.setField(genre1, "title", "동화");
            ReflectionTestUtils.setField(genre1, "isDeleted", false);

            genre2 = genreConstructor.newInstance();
            ReflectionTestUtils.setField(genre2, "id", 2L);
            ReflectionTestUtils.setField(genre2, "title", "과학");
            ReflectionTestUtils.setField(genre2, "isDeleted", false);

            genre3 = genreConstructor.newInstance();
            ReflectionTestUtils.setField(genre3, "id", 3L);
            ReflectionTestUtils.setField(genre3, "title", "역사");
            ReflectionTestUtils.setField(genre3, "isDeleted", false);

            genreScore1 = GenreScore.builder()
                    .kid(kid)
                    .genre(genre1)
                    .score(0)
                    .isDeleted(false)
                    .build();
            ReflectionTestUtils.setField(genreScore1, "id", 1L);

            genreScore2 = GenreScore.builder()
                    .kid(kid)
                    .genre(genre2)
                    .score(0)
                    .isDeleted(false)
                    .build();
            ReflectionTestUtils.setField(genreScore2, "id", 2L);

            genreScore3 = GenreScore.builder()
                    .kid(kid)
                    .genre(genre3)
                    .score(0)
                    .isDeleted(false)
                    .build();
            ReflectionTestUtils.setField(genreScore3, "id", 3L);

        } catch (Exception e) {
            throw new RuntimeException("Test setup failed", e);
        }
    }

    @Test
    @DisplayName("장르 점수 추가에 성공한다")
    void addGenreScore_Success() {
        // Given
        List<Long> genreIds = Arrays.asList(1L, 2L);
        List<Genre> allGenres = Arrays.asList(genre1, genre2, genre3);

        given(kidRepository.findById(1L)).willReturn(Optional.of(kid));
        given(genreRepository.findAll()).willReturn(allGenres);
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre1));
        given(genreRepository.findById(2L)).willReturn(Optional.of(genre2));

        given(genreScoreRepository.findByKidAndGenre(kid, genre1))
                .willReturn(Optional.of(genreScore1));
        given(genreScoreRepository.findByKidAndGenre(kid, genre2))
                .willReturn(Optional.of(genreScore2));
        given(genreScoreRepository.findByKidAndGenre(kid, genre3))
                .willReturn(Optional.of(genreScore3));

        // When
        genreScoreService.addGenreScore(1L, genreIds);

        // Then
        verify(genreScoreRepository, times(5)).save(any(GenreScore.class));
        assertThat(genreScore1.getScore()).isEqualTo(5);
        assertThat(genreScore2.getScore()).isEqualTo(5);
        assertThat(genreScore3.getScore()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 아이의 장르 점수 추가 시 예외가 발생한다")
    void addGenreScore_KidNotFound() {
        // Given
        List<Long> genreIds = Arrays.asList(1L, 2L);
        given(kidRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> genreScoreService.addGenreScore(999L, genreIds))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID);
    }

    @Test
    @DisplayName("아이의 최고 점수 장르 조회에 성공한다")
    void getTopGenre_Success() {
        // Given
        GenreScore topGenreScore = GenreScore.builder()
                .kid(kid)
                .genre(genre1)
                .score(100)
                .isDeleted(false)
                .build();

        given(kidRepository.findById(1L)).willReturn(Optional.of(kid));
        given(genreScoreRepository.findTopGenreByKidId(1L))
                .willReturn(Optional.of(topGenreScore));

        // When
        TopGenreResponse response = genreScoreService.getTopGenre(1L);

        // Then
        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getGenreId()).isEqualTo(genre1.getId());
                    assertThat(r.getGenreTitle()).isEqualTo(genre1.getTitle());
                    assertThat(r.getScore()).isEqualTo(100);
                });
    }

    @Test
    @DisplayName("존재하지 않는 아이의 최고 점수 장르 조회 시 예외가 발생한다")
    void getTopGenre_KidNotFound() {
        // Given
        given(kidRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> genreScoreService.getTopGenre(999L))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID);
    }
}