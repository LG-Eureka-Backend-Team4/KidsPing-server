package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class KakaoAuthCodeExpiredException extends GlobalException {
    public KakaoAuthCodeExpiredException() {
        super(ExceptionCode.KAKAO_AUTH_CODE_EXPIRED);
    }
}