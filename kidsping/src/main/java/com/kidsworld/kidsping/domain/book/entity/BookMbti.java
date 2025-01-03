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

    @Column(name = "e_score")
    private Integer eScore;

    @Column(name = "i_score")
    private Integer iScore;

    @Column(name = "s_score")
    private Integer sScore;

    @Column(name = "n_score")
    private Integer nScore;

    @Column(name = "t_score")
    private Integer tScore;

    @Column(name = "f_score")
    private Integer fScore;

    @Column(name = "j_score")
    private Integer jScore;

    @Column(name = "p_score")
    private Integer pScore;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
