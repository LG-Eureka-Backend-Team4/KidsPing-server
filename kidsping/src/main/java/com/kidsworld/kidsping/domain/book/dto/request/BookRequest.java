package com.kidsworld.kidsping.domain.book.dto.request;

import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class BookRequest {
    private Long genreId;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private Integer age;
    private String imageUrl;
    private MbtiType bookMbtiType;
    private Integer eScore;
    private Integer iScore;
    private Integer sScore;
    private Integer nScore;
    private Integer tScore;
    private Integer fScore;
    private Integer jScore;
    private Integer pScore;
}
