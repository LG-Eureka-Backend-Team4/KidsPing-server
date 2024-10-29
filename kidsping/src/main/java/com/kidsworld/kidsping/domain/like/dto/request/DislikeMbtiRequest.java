package com.kidsworld.kidsping.domain.like.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DislikeMbtiRequest {

    private Long kidId;

    private Long bookId;

    public DislikeMbtiRequest(Long kidId, Long bookId) {
        this.kidId = kidId;
        this.bookId = bookId;
    }
}