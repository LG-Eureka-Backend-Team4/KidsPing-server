package com.kidsworld.kidsping.domain.like.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeGenreRequest {

    private Long kidId;

    private Long bookId;
}
