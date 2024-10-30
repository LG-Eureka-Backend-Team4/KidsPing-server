package com.kidsworld.kidsping.domain.like.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DislikeCancelMbtiRequest {

    private Long kidId;

    private Long bookId;

    public DislikeCancelMbtiRequest(Long kidId, Long bookId) {
        this.kidId = kidId;
        this.bookId = bookId;
    }
}