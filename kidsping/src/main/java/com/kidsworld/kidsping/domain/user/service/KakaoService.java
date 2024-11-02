package com.kidsworld.kidsping.domain.user.service;

import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;

public interface KakaoService {
    LoginResponse handleKakaoLogin(String code);
    void kakaoLogout(String accessToken, String email);
    LoginResponse refreshKakaoUserToken(String email);

}