package com.kidsworld.kidsping.domain.user.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class DuplicateEmailException extends GlobalException {
    public DuplicateEmailException() {
        super(ExceptionCode.DUPLICATE_EMAIL);
    }
}