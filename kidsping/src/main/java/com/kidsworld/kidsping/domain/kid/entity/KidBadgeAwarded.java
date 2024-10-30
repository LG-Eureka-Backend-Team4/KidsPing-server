package com.kidsworld.kidsping.domain.kid.entity;

import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KidBadgeAwarded extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kid_id", nullable = false)
    private Kid kid;

    @ManyToOne
    @JoinColumn(name = "badge_id", nullable = false)
    private KidBadge badge;

    @Builder
    public KidBadgeAwarded(Kid kid, KidBadge badge) {
        this.kid = kid;
        this.badge = badge;
    }
}
