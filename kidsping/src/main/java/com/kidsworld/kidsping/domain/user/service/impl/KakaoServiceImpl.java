package com.kidsworld.kidsping.domain.user.service.impl;

import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.dto.response.GetKakaoTokenResponse;
import com.kidsworld.kidsping.domain.user.dto.response.GetKakaoUserResponse;
import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import com.kidsworld.kidsping.domain.user.exception.GeneralLoginNotAllowedException;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.handler.KakaoTokenHandler;
import com.kidsworld.kidsping.domain.user.service.KakaoService;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final WebClient webClient;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final KakaoTokenHandler tokenHandler;

    @Override
    public LoginResponse handleKakaoLogin(String code) {
        GetKakaoTokenResponse tokens = tokenHandler.getKakaoTokens(code);
        GetKakaoUserResponse userInfo = getKakaoUserInfo(tokens.getAccess_token());
        return loginOrSignup(userInfo, tokens.getRefresh_token(), tokens.getAccess_token());
    }

    private GetKakaoUserResponse getKakaoUserInfo(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        return webClient.post()
                .uri(userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(GetKakaoUserResponse.class)
                .block();
    }

    private LoginResponse loginOrSignup(GetKakaoUserResponse kakaoUserInfo, String refreshToken, String accessToken) {
        String email = (kakaoUserInfo.getKakao_account() != null &&
                kakaoUserInfo.getKakao_account().getEmail() != null) ?
                kakaoUserInfo.getKakao_account().getEmail() :
                "kakao_" + kakaoUserInfo.getId() + "@temp.com";

        String username = "User_" + kakaoUserInfo.getId();

        Optional<User> existingUser = userService.findBySocialId(String.valueOf(kakaoUserInfo.getId()));

        User user;
        if (existingUser.isEmpty()) {
            RegisterRequest registerRequest = RegisterRequest.builder()
                    .email(email)
                    .username(username)
                    .socialId(String.valueOf(kakaoUserInfo.getId()))
                    .password("")
                    .role(Role.USER)
                    .build();

            user = userService.save(registerRequest);
        } else {
            user = existingUser.get();
        }

        // 토큰 저장
        user.updateKakaoAccessToken(accessToken);
        tokenHandler.storeRefreshToken(user.getId(), refreshToken);

        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(user.getEmail(), "", new ArrayList<>())
        );

        List<GetKidListResponse> kidsList = userService.getKidsList(user.getId());

        return LoginResponse.builder()
                .email(user.getEmail())
                .jwt(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .data(kidsList)
                .build();
    }

    @Override
    public void kakaoLogout(String token, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        String logoutUri = "https://kapi.kakao.com/v1/user/logout";

        webClient.post()
                .uri(logoutUri)
                .header("Authorization", "Bearer " + user.getKakaoAccessToken())  // 저장된 카카오 액세스 토큰 사용
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        user.removeKakaoTokens();
        user.removeRefreshToken();
        userService.update(user);
    }




    public LoginResponse refreshKakaoUserToken(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 일반 로그인 사용자의 접근 차단
        if (user.getSocialId() == null) {
            throw new GeneralLoginNotAllowedException();
        }

        try {
            GetKakaoTokenResponse newTokens = tokenHandler.refreshKakaoToken(user);
            user.updateKakaoAccessToken(newTokens.getAccess_token());
            userService.update(user);

            String newJwtToken = jwtUtil.generateToken(
                    new org.springframework.security.core.userdetails.User(email, "", new ArrayList<>())
            );

            List<GetKidListResponse> kidsList = userService.getKidsList(user.getId());

            return LoginResponse.builder()
                    .email(email)
                    .jwt(newJwtToken)
                    .refreshToken(newTokens.getRefresh_token())
                    .userId(user.getId())
                    .data(kidsList)
                    .build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("refresh_token_expired")) {
                throw new RuntimeException("refresh_token_expired");
            }
            throw e;
        }
    }



}
