package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException() {
        super(ExceptionCode.UNAUTHORIZED_USER);
    }
}