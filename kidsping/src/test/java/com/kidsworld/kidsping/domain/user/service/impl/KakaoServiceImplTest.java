package com.kidsworld.kidsping.domain.user.service.impl;

import com.kidsworld.kidsping.domain.user.dto.response.GetKakaoTokenResponse;
import com.kidsworld.kidsping.domain.user.dto.response.GetKakaoUserResponse;
import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.handler.KakaoTokenHandler;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private KakaoTokenHandler tokenHandler;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private KakaoServiceImpl kakaoService;

    @Test
    @DisplayName("카카오 로그인 성공 시 신규 회원을 생성하고 로그인 응답을 반환한다")
    void 카카오로그인_성공() {
        // Given
        String authCode = "test_auth_code";
        String accessToken = "test_access_token";
        String refreshToken = "test_refresh_token";
        Long kakaoId = 12345L;
        String email = "test@kakao.com";
        Long userId = 1L;
        String jwtToken = "test_jwt_token";

        // Mock KakaoTokenResponse
        GetKakaoTokenResponse tokenResponse = new GetKakaoTokenResponse(
                accessToken, "bearer", refreshToken, 3600, "test_scope", 3600);
        when(tokenHandler.getKakaoTokens(authCode)).thenReturn(tokenResponse);

        // Mock KakaoUserResponse
        GetKakaoUserResponse.KakaoAccount kakaoAccount = new GetKakaoUserResponse.KakaoAccount();
        GetKakaoUserResponse userResponse = new GetKakaoUserResponse();
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GetKakaoUserResponse.class)).thenReturn(reactor.core.publisher.Mono.just(userResponse));

        // Mock User and LoginResponse
        User savedUser = User.builder()
                .id(userId)
                .email(email)
                .socialId(String.valueOf(kakaoId))
                .build();
        when(userService.findBySocialId(anyString())).thenReturn(Optional.empty());
        when(userService.save(any())).thenReturn(savedUser);
        when(jwtUtil.generateToken(any())).thenReturn(jwtToken);

        GetKidListResponse kidResponse = GetKidListResponse.builder()
                .kidId(1L)
                .name("Test Kid")
                .profileUrl(null)
                .build();
        when(userService.getKidsList(userId)).thenReturn(Arrays.asList(kidResponse));

        // When
        LoginResponse response = kakaoService.handleKakaoLogin(authCode);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJwt()).isEqualTo(jwtToken);
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData().get(0).getKidId()).isEqualTo(1L);
        assertThat(response.getData().get(0).getName()).isEqualTo("Test Kid");

        verify(tokenHandler).getKakaoTokens(authCode);
        verify(tokenHandler).storeRefreshToken(userId, refreshToken);
        verify(userService).save(any());
        verify(userService).getKidsList(userId);
    }


    @Test
    @DisplayName("카카오 로그아웃을 성공적으로 수행한다")
    void 카카오로그아웃_성공() {
        // Given
        String token = "test_token";
        String email = "test@kakao.com";
        String kakaoAccessToken = "kakao_access_token";

        User user = User.builder()
                .id(1L)
                .email(email)
                .kakaoAccessToken(kakaoAccessToken)
                .build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.just(""));

        // When
        kakaoService.kakaoLogout(token, email);

        // Then
        verify(userService).findByEmail(email);
        verify(userService).update(argThat(updatedUser ->
                updatedUser.getKakaoAccessToken() == null &&
                        updatedUser.getRefreshToken() == null
        ));
    }

    @Test
    @DisplayName("카카오 회원의 리프레시 토큰을 성공적으로 갱신한다")
    void 카카오회원리프레시토큰발급_성공() {
        // Given
        String email = "test@kakao.com";
        String oldRefreshToken = "old_refresh_token";
        String newAccessToken = "new_access_token";
        String newRefreshToken = "new_refresh_token";
        String newJwtToken = "new_jwt_token";
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email(email)
                .socialId("12345")
                .refreshToken(oldRefreshToken)
                .build();

        GetKakaoTokenResponse newTokens = new GetKakaoTokenResponse(
                newAccessToken, "bearer", newRefreshToken, 3600, "test_scope", 3600);

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenHandler.refreshKakaoToken(user)).thenReturn(newTokens);
        when(jwtUtil.generateToken(any())).thenReturn(newJwtToken);

        GetKidListResponse kidResponse = GetKidListResponse.builder()
                .kidId(1L)
                .name("Test Kid")
                .profileUrl(null)
                .build();
        when(userService.getKidsList(userId)).thenReturn(Arrays.asList(kidResponse));

        // When
        LoginResponse response = kakaoService.refreshKakaoUserToken(email);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJwt()).isEqualTo(newJwtToken);
        assertThat(response.getRefreshToken()).isEqualTo(newRefreshToken);
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getData()).hasSize(1);

        verify(userService).findByEmail(email);
        verify(tokenHandler).refreshKakaoToken(user);
        verify(userService).update(argThat(updatedUser ->
                updatedUser.getKakaoAccessToken().equals(newAccessToken)
        ));
    }
}