package com.kidsworld.kidsping.domain.kid.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class InvalidRequestFormatException extends GlobalException {

    public InvalidRequestFormatException() {
        super(ExceptionCode.INVALID_REQUEST_FORMAT);
    }

}