package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponse;
import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import com.kidsworld.kidsping.domain.question.repository.GenreAnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GenreAnswerServiceImplTest {

    @InjectMocks
    private GenreAnswerServiceImpl genreAnswerService;

    @Mock
    private GenreAnswerRepository genreAnswerRepository;

    @Mock
    private KidRepository kidRepository;

    private Kid kid;
    private GenreAnswer genreAnswer;
    private LocalDateTime now;

    @BeforeEach
    void setUp() throws Exception {
        now = LocalDateTime.now();

        Constructor<Kid> kidConstructor = Kid.class.getDeclaredConstructor();
        kidConstructor.setAccessible(true);
        kid = kidConstructor.newInstance();
        ReflectionTestUtils.setField(kid, "id", 1L);
        ReflectionTestUtils.setField(kid, "isDeleted", false);

        genreAnswer = GenreAnswer.builder()
                .kid(kid)
                .isDeleted(false)
                .build();
        ReflectionTestUtils.setField(genreAnswer, "id", 1L);
        ReflectionTestUtils.setField(genreAnswer, "createdAt", now);
    }

    @Test
    @DisplayName("아이의 장르 응답 이력 조회에 성공한다")
    void getGenreAnswerHistory_Success() {
        // Given
        List<GenreAnswer> genreAnswers = Arrays.asList(genreAnswer);
        given(kidRepository.findById(1L)).willReturn(Optional.of(kid));
        given(genreAnswerRepository.findByKidIdAndIsDeletedFalse(1L)).willReturn(genreAnswers);

        // When
        List<GenreAnswerResponse> responses = genreAnswerService.getGenreAnswerHistory(1L);

        // Then
        assertThat(responses).hasSize(1)
                .first()
                .satisfies(response -> {
                    assertThat(response.getKidId()).isEqualTo(1L);
                    assertThat(response.getCreatedAt()).isEqualTo(now);
                });

        verify(kidRepository).findById(1L);
        verify(genreAnswerRepository).findByKidIdAndIsDeletedFalse(1L);
    }
}