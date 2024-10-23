package com.kidsworld.kidsping.domain.like.dto.response;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeBookResponse {
    private Long id;
    private Long genreId;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private Integer age;
    private String imageUrl;
    private MbtiType mbtiType;

    public static LikeBookResponse from(Book book) {
        return LikeBookResponse.builder()
                .id(book.getId())
                .genreId(book.getGenre().getId())
                .title(book.getTitle())
                .summary(book.getSummary())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .age(book.getAge())
                .imageUrl(book.getImageUrl())
                .mbtiType(book.getBookMbti().getBookMbtiType())
                .build();
    }
}
