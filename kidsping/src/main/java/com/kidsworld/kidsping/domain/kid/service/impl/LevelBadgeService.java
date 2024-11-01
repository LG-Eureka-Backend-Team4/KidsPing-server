package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidBadge;
import com.kidsworld.kidsping.domain.kid.entity.KidBadgeAwarded;
import com.kidsworld.kidsping.domain.kid.repository.KidBadgeAwardedRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidBadgeRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeGenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LevelBadgeService {

    private final KidRepository kidRepository;
    private final KidBadgeRepository kidBadgeRepository;
    private final KidBadgeAwardedRepository kidBadgeAwardedRepository;
    private final LikeGenreRepository likeGenreRepository;

    public LevelBadgeService(KidRepository kidRepository, KidBadgeRepository kidBadgeRepository,
                             KidBadgeAwardedRepository kidBadgeAwardedRepository, LikeGenreRepository likeGenreRepository) {
        this.kidRepository = kidRepository;
        this.kidBadgeRepository = kidBadgeRepository;
        this.kidBadgeAwardedRepository = kidBadgeAwardedRepository;
        this.likeGenreRepository = likeGenreRepository;
    }

    @Transactional
    public void checkAndAssignLevelAndBadge(Long kidId) {
        Kid kid = kidRepository.findById(kidId).orElseThrow(() -> new IllegalArgumentException("Kid not found"));
        int likeCount = likeGenreRepository.countLikesByKidId(kidId, LikeStatus.LIKE);
        int calculatedLevel = likeCount / 10;
        assignBadgeForLevel(kid, calculatedLevel);
    }

    private void assignBadgeForLevel(Kid kid, int level) {
        if (level == 1 || level == 3 || level == 5 || level == 8) {
            KidBadge badge = kidBadgeRepository.findByRequiredLevel(level);
            if (badge != null && !kidBadgeAwardedRepository.existsByKidAndBadge(kid, badge)) {
                KidBadgeAwarded awarded = KidBadgeAwarded.builder()
                        .kid(kid)
                        .badge(badge)
                        .build();
                kidBadgeAwardedRepository.save(awarded);
            }
        }
    }

    public int getCurrentLevel(Long kidId) {
        int likeCount = likeGenreRepository.countLikesByKidId(kidId, LikeStatus.LIKE);
        return likeCount / 10; // 10개 좋아요당 1레벨로 계산
    }
}
