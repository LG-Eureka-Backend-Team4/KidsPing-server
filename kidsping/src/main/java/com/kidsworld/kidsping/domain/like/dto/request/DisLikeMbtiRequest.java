package com.kidsworld.kidsping.domain.like.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisLikeMbtiRequest {

    private Long kidId;

    private Long bookId;
}