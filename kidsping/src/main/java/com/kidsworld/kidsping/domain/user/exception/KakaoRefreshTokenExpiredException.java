package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class KakaoRefreshTokenExpiredException extends GlobalException {
    public KakaoRefreshTokenExpiredException() {
        super(ExceptionCode.KAKAO_REFRESH_TOKEN_EXPIRED);
    }
}