package com.kidsworld.kidsping.global.aop;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MbtiDiagnosisAspectTest {

    @InjectMocks
    private MbtiDiagnosisAspect mbtiDiagnosisAspect;

    @Mock
    private KidRepository kidRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JoinPoint joinPoint;

    private Kid kid;
    private KidMbti kidMbti;

    @BeforeEach
    void setUp() {
        kid = mock(Kid.class);
        kidMbti = mock(KidMbti.class);
    }

    @Test
    @DisplayName("기본 조회 API - 검증 제외")
    void skipBasicGet() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/books/1");
        when(request.getMethod()).thenReturn("GET");

        // When & Then
        assertThatNoException()
                .isThrownBy(() -> mbtiDiagnosisAspect.validateMbtiDiagnosis(joinPoint));
        verify(kidRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("관리자 API - 검증 제외")
    void skipAdmin() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/books/1");
        when(request.getMethod()).thenReturn("PUT");

        // When & Then
        assertThatNoException()
                .isThrownBy(() -> mbtiDiagnosisAspect.validateMbtiDiagnosis(joinPoint));
        verify(kidRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("kidId가 null이면 INVALID_REQUEST 예외가 발생한다")
    void validateMbtiDiagnosis_InvalidKidId() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/books/kid/null/genre/1");

        // When & Then
        assertThatThrownBy(() -> mbtiDiagnosisAspect.validateMbtiDiagnosis(joinPoint))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.INVALID_REQUEST);
        verify(kidRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 kidId인 경우 NOT_FOUND_KID 예외가 발생한다")
    void validateMbtiDiagnosis_KidNotFound() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/books/kid/1/mbti");
        when(kidRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> mbtiDiagnosisAspect.validateMbtiDiagnosis(joinPoint))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.NOT_FOUND_KID);
        verify(kidRepository).findById(1L);
    }

    @Test
    @DisplayName("MBTI 진단이 없는 경우 MBTI_DIAGNOSIS_REQUIRED 예외가 발생한다")
    void validateMbtiDiagnosis_NoMbti() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/books/kid/1/genre");
        when(kid.getKidMbti()).thenReturn(null);
        when(kidRepository.findById(1L)).thenReturn(Optional.of(kid));

        // When & Then
        assertThatThrownBy(() -> mbtiDiagnosisAspect.validateMbtiDiagnosis(joinPoint))
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorExceptionCode", ExceptionCode.MBTI_DIAGNOSIS_REQUIRED)
                .hasMessage("서비스를 이용하기 위해서 자녀 성향 진단이 필요합니다.");
        verify(kidRepository).findById(1L);
    }

    @Test
    @DisplayName("MBTI 진단이 완료된 정상 요청은 검증을 통과한다")
    void validateMbtiDiagnosis_Success() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/books/kid/1/combi");
        when(kid.getKidMbti()).thenReturn(kidMbti);
        when(kidRepository.findById(1L)).thenReturn(Optional.of(kid));

        // When & Then
        assertThatNoException().isThrownBy(() -> mbtiDiagnosisAspect.validateMbtiDiagnosis(joinPoint));
        verify(kidRepository).findById(1L);
    }
}