package com.kidsworld.kidsping.domain.mbti.entity;

import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
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

    private boolean isDeleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_model", length = 50)
    private RoleModel roleModel;


    @OneToOne
    @JoinColumn(name="file_id")
    private MbtiFile mbtifile;
}
