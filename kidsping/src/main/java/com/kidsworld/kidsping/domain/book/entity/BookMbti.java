package com.kidsworld.kidsping.domain.book.entity;

import com.kidsworld.kidsping.domain.book.enums.MBTIType;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiScore;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMbti extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_mbti_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "mbti_score_id", nullable = false)
    private MbtiScore mbtiScoreId;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_mbti_type")
    private MBTIType bookMbtiType;

    @Column(name = "mbti_description", columnDefinition = "TEXT")
    private String mbtiDescription;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
