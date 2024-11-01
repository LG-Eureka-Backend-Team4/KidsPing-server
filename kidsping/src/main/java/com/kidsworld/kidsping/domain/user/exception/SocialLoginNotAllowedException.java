package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class SocialLoginNotAllowedException extends GlobalException {
    public SocialLoginNotAllowedException() {
        super(ExceptionCode.SOCIAL_LOGIN_NOT_ALLOWED);
    }
}