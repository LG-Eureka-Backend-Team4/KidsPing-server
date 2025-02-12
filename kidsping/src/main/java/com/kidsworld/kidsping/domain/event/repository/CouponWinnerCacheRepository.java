package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponWinnerCacheRepository {

    private final ConcurrentHashMap<Long, Set<Long>> couponWinnerCacheStore = new ConcurrentHashMap<>();
    private final CouponRepository couponRepository;
    private final ReentrantLock lock = new ReentrantLock();

//    public boolean findWinnersIfAbsent(Long eventId, Long userId) {
//        if (couponWinnerCacheStore.isEmpty()) {
//            try {
//                if (lock.tryLock(10, TimeUnit.SECONDS)) {
//                    try {
//                        if (couponWinnerCacheStore.isEmpty()) {
//                            updateTodayWinners();
//                        }
//                    } finally {
//                        lock.unlock();
//                    }
//                } else {
//                    log.warn("Lock 획득 실패 - 다른 스레드에서 캐시를 업데이트 중");
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                log.error("Lock 대기 중 인터럽트 발생", e);
//            }
//        }
//
//        return couponWinnerCacheStore.containsKey(eventId) &&
//                couponWinnerCacheStore.get(eventId).contains(userId);
//    }

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

//    public boolean findWinnersIfAbsent(Long eventId, Long userId) {
//        if (couponWinnerCacheStore.isEmpty()) {
//            updateTodayWinners();
//        }
//        return couponWinnerCacheStore.containsKey(eventId) && couponWinnerCacheStore.get(eventId).contains(userId);
//    }

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