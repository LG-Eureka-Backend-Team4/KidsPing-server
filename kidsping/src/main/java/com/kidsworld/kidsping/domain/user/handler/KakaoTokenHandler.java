package com.kidsworld.kidsping.domain.user.handler;

import com.kidsworld.kidsping.domain.user.dto.response.GetKakaoTokenResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.*;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoTokenHandler {
    private final WebClient webClient;
    private final UserRepository userRepository;

    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;


    /**
     인증 코드로 카카오 토큰 받기
     */
    public GetKakaoTokenResponse getKakaoTokens(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        log.info("=== 카카오 토큰 요청 시작 ===");
        log.info("토큰 URI: {}", tokenUri);
        log.info("클라이언트 ID: {}", clientId);
        log.info("리디렉트 URI: {}", redirectUri);
        log.info("인증 코드: {}", code);
        log.info("전체 요청 파라미터: {}", params);

        try {
            GetKakaoTokenResponse response = webClient
                    .post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(GetKakaoTokenResponse.class)
                    .block();

            log.info("카카오 토큰 응답 수신 완료: {}", response);
            return response;
        } catch (WebClientResponseException e) {
            log.error("카카오 토큰 요청 실패. 상태 코드: {}, 응답 내용: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

            if (e.getResponseBodyAsString().contains("KOE320")) {
                throw new KakaoAuthCodeExpiredException();
            }
            throw new KakaoLoginFailedException();
        }
    }

    /**
     리프레시 토큰으로 새 토큰 발급
     */
    private GetKakaoTokenResponse getNewToken(String refreshToken) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", clientId);
        params.add("refresh_token", refreshToken);

        try {
            return webClient
                    .post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(GetKakaoTokenResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("카카오 토큰 갱신 실패: {}", e.getResponseBodyAsString());
            throw new KakaoRefreshTokenExpiredException();
        }
    }

    /**
    토큰 갱신 및 저장
     */
    public GetKakaoTokenResponse refreshKakaoToken(User user) {
        if (user.getRefreshToken() == null) {
            throw new KakaoTokenExpiredException();
        }

        try {
            GetKakaoTokenResponse tokens = getNewToken(user.getRefreshToken());
            if (tokens.getRefresh_token() != null) {
                user.updateRefreshToken(tokens.getRefresh_token());
                userRepository.save(user);
            }
            return tokens;
        } catch (WebClientResponseException e) {
            user.removeRefreshToken();
            userRepository.save(user);
            throw new KakaoRefreshTokenExpiredException();
        }
    }


    /**
    리프레시 토큰 저장
     */
    public void storeRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }


}