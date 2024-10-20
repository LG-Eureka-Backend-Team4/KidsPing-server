package com.kidsworld.kidsping.domain.mbti.entity;

import com.kidsworld.kidsping.domain.kid.entity.enums.MbtiStatus;
import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_info_id")
    private Long id;

    private MbtiStatus mbtiStatus;

    private String type;

    private String title;

    @Enumerated(EnumType.STRING)
    private RoleModel roleModel;

    private String hashtag;

    private String description;
}
