package com.kidsworld.kidsping.domain.book.dto.response;

import com.kidsworld.kidsping.domain.book.entity.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponseDto {
    private Long id;
    private Long genreId;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private Integer age;
    private String imageUrl;

    public static BookResponseDto from(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .genreId(book.getGenre().getId())
                .title(book.getTitle())
                .summary(book.getSummary())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .age(book.getAge())
                .imageUrl(book.getImageUrl())
                .build();
    }
}
