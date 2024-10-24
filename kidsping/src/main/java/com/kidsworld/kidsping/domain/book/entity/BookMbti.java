package com.kidsworld.kidsping.domain.book.entity;

import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookMbti extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_mbti_type", nullable = false)
    private MbtiType bookMbtiType;

    @Range(min = 0, max = 100)
    @Column(name = "e_score")
    private Integer eScore;

    @Range(min = 0, max = 100)
    @Column(name = "i_score")
    private Integer iScore;

    @Range(min = 0, max = 100)
    @Column(name = "s_score")
    private Integer sScore;

    @Range(min = 0, max = 100)
    @Column(name = "n_score")
    private Integer nScore;

    @Range(min = 0, max = 100)
    @Column(name = "t_score")
    private Integer tScore;

    @Range(min = 0, max = 100)
    @Column(name = "f_score")
    private Integer fScore;

    @Range(min = 0, max = 100)
    @Column(name = "j_score")
    private Integer jScore;

    @Range(min = 0, max = 100)
    @Column(name = "p_score")
    private Integer pScore;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
