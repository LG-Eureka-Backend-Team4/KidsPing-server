package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class KakaoLoginFailedException extends GlobalException {
    public KakaoLoginFailedException() {
        super(ExceptionCode.KAKAO_LOGIN_FAILED);
    }
}