package com.kidsworld.kidsping.domain.kid.entity;

import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KidBadge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    private String badgeName;
    private String description;
    private int requiredLevel;
    private String imageUrl;
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public KidBadge(String badgeName, String description, int requiredLevel, String imageUrl, boolean isDeleted) {
        this.badgeName = badgeName;
        this.description = description;
        this.requiredLevel = requiredLevel;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }
}