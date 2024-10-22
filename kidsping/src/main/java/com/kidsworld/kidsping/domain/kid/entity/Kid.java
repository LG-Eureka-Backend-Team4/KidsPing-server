package com.kidsworld.kidsping.domain.kid.entity;

import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kid extends BaseTimeEntity {

    @Id
    @Column(name = "kid_id")
    private Long id;

    private Gender gender;

    private String name;

    private LocalDate birth;

    private boolean isDeleted = Boolean.FALSE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="file_id")
    private KidFile kidfile;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_mbti_id")
    private KidMbti kidMbti;


    public void update(Gender gender, String name, LocalDate birth) {
        this.gender = gender;
        this.name = name;
        this.birth = birth;
    }


}