package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class GeneralLoginNotAllowedException extends GlobalException {
    public GeneralLoginNotAllowedException() {
        super(ExceptionCode.GENERAL_LOGIN_NOT_ALLOWED);
    }
}