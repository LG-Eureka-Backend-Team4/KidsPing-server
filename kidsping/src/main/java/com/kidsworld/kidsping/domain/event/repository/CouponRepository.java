package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByUserIdAndEventId(Long userId, Long eventId);

    @Query("SELECT c FROM Coupon c "
            + "join fetch c.user u "
            + "join fetch c.event e "
            + "WHERE c.createdAt >= :startOfDay AND c.createdAt <= :currentTime "
            + "AND u.isDeleted = false "
            + "AND e.isDeleted = false "
            + "AND c.isDeleted = false ")
    List<Coupon> findCouponsCreatedTodayBeforeNow(@Param("startOfDay") LocalDateTime startOfDay,
                                                  @Param("currentTime") LocalDateTime currentTime);

}
