package com.kidsworld.kidsping.domain.genre.dto.response;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.entity.GenreScore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TopGenreResponse {
    private Long genreId;
    private String genreTitle;
    private Integer score;

    public static TopGenreResponse from(Genre genre, GenreScore genreScore) {
        return TopGenreResponse.builder()
                .genreId(genre.getId())
                .genreTitle(genre.getTitle())
                .score(genreScore.getScore())
                .build();
    }
}
