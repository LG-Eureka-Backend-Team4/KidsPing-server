package com.kidsworld.kidsping.domain.book.dto.response;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private Long genreId;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private Integer age;
    private String imageUrl;
    private MbtiType mbtiType;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
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
