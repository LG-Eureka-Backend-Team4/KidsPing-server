package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidBadge;
import com.kidsworld.kidsping.domain.kid.entity.KidBadgeAwarded;
import com.kidsworld.kidsping.domain.kid.repository.KidBadgeAwardedRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidBadgeRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeGenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelBadgeServiceTest {

    @Mock
    private KidRepository kidRepository;

    @Mock
    private KidBadgeRepository kidBadgeRepository;

    @Mock
    private KidBadgeAwardedRepository kidBadgeAwardedRepository;

    @Mock
    private LikeGenreRepository likeGenreRepository;

    @InjectMocks
    private LevelBadgeService levelBadgeService;

    private Kid kid;

    @BeforeEach
    void setUp() {
        kid = Kid.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("Kid가 10개 이상의 좋아요를 가지고 있을 때 레벨에 맞는 배지가 할당되는지 확인")
    void checkAndAssignLevelAndBadge() {
        // Given: Kid가 존재하고 10개의 좋아요를 갖고 있어 레벨 1에 해당
        when(kidRepository.findById(kid.getId())).thenReturn(Optional.of(kid));
        when(likeGenreRepository.countLikesByKidId(kid.getId(), LikeStatus.LIKE)).thenReturn(10);

        // 특정 레벨에서 배지 조회 및 배지 부여 상황 설정
        KidBadge badge = KidBadge.builder()
                .requiredLevel(1)
                .build();

        when(kidBadgeRepository.findByRequiredLevel(1)).thenReturn(badge);
        when(kidBadgeAwardedRepository.existsByKidAndBadge(kid, badge)).thenReturn(false);

        // When: 레벨과 배지 할당 메소드 호출
        levelBadgeService.checkAndAssignLevelAndBadge(kid.getId());

        // Then: 배지가 주어졌는지 확인
        verify(kidBadgeAwardedRepository, times(1)).save(any(KidBadgeAwarded.class));
    }

    @Test
    @DisplayName("Kid의 좋아요 수가 50개일 때 현재 레벨이 5인지 확인")
    void getCurrentLevel() {
        // Given: 50개의 좋아요가 있을 때
        when(likeGenreRepository.countLikesByKidId(kid.getId(), LikeStatus.LIKE)).thenReturn(50);

        // When: 현재 레벨을 계산
        int level = levelBadgeService.getCurrentLevel(kid.getId());

        // Then: 레벨이 5인지 확인
        assertEquals(5, level);
    }
}
