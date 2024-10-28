package com.kidsworld.kidsping.domain.mbti.entity;

import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mbti_info")
public class MbtiInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_info_id")
    private Long id;

    @Column(name = "mbti_status", length = 4)
    private String mbtiStatus;

    private String title;

    private String description;

    private final boolean isDeleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_model", length = 50)
    private RoleModel roleModel;

    private String imageUrl;
}
