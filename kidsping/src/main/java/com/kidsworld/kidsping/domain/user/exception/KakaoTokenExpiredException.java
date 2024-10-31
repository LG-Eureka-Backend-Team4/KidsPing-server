package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class KakaoTokenExpiredException extends GlobalException {
    public KakaoTokenExpiredException() {
        super(ExceptionCode.KAKAO_TOKEN_EXPIRED);
    }
}