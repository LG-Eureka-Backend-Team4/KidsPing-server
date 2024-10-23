package com.kidsworld.kidsping.domain.kid.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class MaxKidLimitReachedException extends GlobalException {

    public MaxKidLimitReachedException () {
        super(ExceptionCode.MAX_KID_LIMIT_REACHED);
    }

}
