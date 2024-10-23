package com.kidsworld.kidsping.domain.mbti.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class NotFoundMbtiInfoException extends GlobalException {

    public NotFoundMbtiInfoException(String message) {
        super(ExceptionCode.NOT_FOUND_MBTI_INFO, message);
    }
}
