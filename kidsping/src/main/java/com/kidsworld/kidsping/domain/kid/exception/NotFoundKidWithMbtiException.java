package com.kidsworld.kidsping.domain.kid.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class NotFoundKidWithMbtiException extends GlobalException {

    public NotFoundKidWithMbtiException(ExceptionCode baseExceptionCode) {
        super(baseExceptionCode);
    }
}
