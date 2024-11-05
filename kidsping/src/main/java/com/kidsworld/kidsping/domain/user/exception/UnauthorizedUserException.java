package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class UnauthorizedUserException extends GlobalException {

    public UnauthorizedUserException() {
        super(ExceptionCode.UNAUTHORIZED_USER);
    }
}
