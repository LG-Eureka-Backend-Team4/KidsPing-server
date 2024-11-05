package com.kidsworld.kidsping.domain.genre.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GenreScoreRequest {
    private final Long kidId;
    private final List<Long> genreIds;
}