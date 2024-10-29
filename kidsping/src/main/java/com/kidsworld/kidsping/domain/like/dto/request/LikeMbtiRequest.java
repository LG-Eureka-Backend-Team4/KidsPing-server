package com.kidsworld.kidsping.domain.like.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeMbtiRequest {

    private Long kidId;

    private Long bookId;

    public LikeMbtiRequest(Long kidId, Long bookId) {
        this.kidId = kidId;
        this.bookId = bookId;
    }
}