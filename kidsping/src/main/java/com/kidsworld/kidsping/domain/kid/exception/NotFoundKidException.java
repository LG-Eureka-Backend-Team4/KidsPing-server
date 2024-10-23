package com.kidsworld.kidsping.domain.kid.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class NotFoundKidException extends GlobalException {

    public NotFoundKidException() {
        super(ExceptionCode.NOT_FOUND_KID);
    }


}
