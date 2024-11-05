package com.kidsworld.kidsping.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetKakaoUserResponse {
    private Long id;
    private KakaoAccount kakao_account;

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private String profile_nickname;
        private String email;
    }
}