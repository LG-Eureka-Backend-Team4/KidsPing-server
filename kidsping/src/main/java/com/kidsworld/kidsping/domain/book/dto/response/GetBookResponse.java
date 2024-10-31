package com.kidsworld.kidsping.domain.book.dto.response;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBookResponse {
    private BookResponse bookInfo;
    private LikeStatus likeStatus;

    public static GetBookResponse of(Book book, LikeStatus likeStatus) {
        return GetBookResponse.builder()
                .bookInfo(BookResponse.from(book))
                .likeStatus(likeStatus)
                .build();
    }
}
