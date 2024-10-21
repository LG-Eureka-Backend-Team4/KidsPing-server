package com.kidsworld.kidsping.domain.book.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookRequestDto {
    private Long genreId;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private Integer age;
    private String imageUrl;
}
