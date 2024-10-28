package com.kidsworld.kidsping.global.exception.custom;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class FileException extends GlobalException {

    public FileException(ExceptionCode errorCode) {
        super(errorCode);
    }
}