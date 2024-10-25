package com.kidsworld.kidsping.domain.event.exception;

import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;

public class EventNotFoundException extends GlobalException {
    public EventNotFoundException() {
        super(ExceptionCode.NOT_FOUND_EVENT);
    }
}