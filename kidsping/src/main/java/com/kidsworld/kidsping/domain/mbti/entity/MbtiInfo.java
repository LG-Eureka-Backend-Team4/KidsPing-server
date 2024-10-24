package com.kidsworld.kidsping.domain.mbti.entity;

import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.FileInfo;

@Entity
@Getter
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
