package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class SocialLoginNotAllowedException extends GlobalException {
    public SocialLoginNotAllowedException() {

        super(ExceptionCode.SOCIAL_LOGIN_NOT_ALLOWED,"소셜 로그인 사용자는 이 메서드를 사용할 수 없습니다.");
    }
}