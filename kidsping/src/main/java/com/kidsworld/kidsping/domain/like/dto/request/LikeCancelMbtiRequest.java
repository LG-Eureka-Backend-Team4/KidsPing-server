package com.kidsworld.kidsping.domain.like.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeCancelMbtiRequest {

    private Long kidId;

    private Long bookId;

    public LikeCancelMbtiRequest(Long kidId, Long bookId) {
        this.kidId = kidId;
        this.bookId = bookId;
    }
}