package com.kidsworld.kidsping.domain.like.dto.response;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeBookResponse {
    private Long id;
    private Long genreId;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private String imageUrl;
    private Integer age;
    private MbtiType mbtiType;

    @Builder
    public LikeBookResponse(Long id, Long genreId, String title, String summary, String author, String publisher,
                            String imageUrl, Integer age, MbtiType mbtiType) {
        this.id = id;
        this.genreId = genreId;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
        this.age = age;
        this.mbtiType = mbtiType;
    }

    public static LikeBookResponse from(Book book) {
        return LikeBookResponse.builder()
                .id(book.getId())
                .genreId(book.getGenre().getId())
                .title(book.getTitle())
                .summary(book.getSummary())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .imageUrl(book.getImageUrl())
                .age(book.getAge())
                .mbtiType(book.getBookMbti().getBookMbtiType())
                .build();
    }
}
