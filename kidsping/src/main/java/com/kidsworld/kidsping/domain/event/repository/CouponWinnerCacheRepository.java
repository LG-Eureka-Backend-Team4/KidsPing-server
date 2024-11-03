package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponWinnerCacheRepository {

    private volatile ConcurrentHashMap<Long, Set<Long>> couponWinnerCacheStore = new ConcurrentHashMap<>();
    private final CouponRepository couponRepository;

    public boolean findWinnersIfAbsent(Long eventId, Long userId) {
        if (couponWinnerCacheStore.isEmpty()) {
            synchronized (this) {
                if (couponWinnerCacheStore.isEmpty()) {
                    updateTodayWinners();
                }
            }
        }
        return couponWinnerCacheStore.containsKey(eventId) && couponWinnerCacheStore.get(eventId).contains(userId);
    }

    // 새벽 2시에 당첨자 캐시 초기화
    @Scheduled(cron = "0 0 2 * * *")
    private void clearCouponWinnerCache() {
        couponWinnerCacheStore.clear();
        log.info("Coupon winner cache has been cleared at 2 AM.");
    }

    // 새벽 2시 30분에 오늘 날짜 당첨자를 저장
    @Scheduled(cron = "0 30 2 * * *")
    private void updateTodayWinners() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();
        List<Coupon> winners = couponRepository.findCouponsCreatedTodayBeforeNow(startOfDay,
                currentTime);
        for (Coupon coupon : winners) {
            Long eventId = coupon.getEvent().getId();
            Long userId = coupon.getUser().getId();
            couponWinnerCacheStore
                    .computeIfAbsent(eventId, k -> new ConcurrentSkipListSet<>())
                    .add(userId);
        }
    }
}