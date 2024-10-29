package com.kidsworld.kidsping.domain.like.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class EmpathyStatusConflictException extends GlobalException {

    public EmpathyStatusConflictException(ExceptionCode baseExceptionCode) {
        super(baseExceptionCode);
    }
}
