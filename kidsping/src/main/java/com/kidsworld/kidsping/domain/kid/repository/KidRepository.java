package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface KidRepository extends JpaRepository<Kid, Long> {
    long countByUserId(Long userId);

    @Query("select k from Kid k join fetch k.kidMbti where k.id = :kidId and k.isDeleted = false ")
    Optional<Kid> findKidWithMbtiByKidId(@Param("kidId") Long kidId);

    @Query("select k from Kid k where k.id = :kidId and k.isDeleted = false")
    Optional<Kid> findKidBy(@Param("kidId") Long kidId);

    @Modifying
    @Query(value = "delete from kid k where k.is_deleted = true and k.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredKid(@Param("currentDate") LocalDateTime currentDate);



    @Modifying
    @Query("UPDATE Kid k SET k.isDeleted = true WHERE k.id = :kidId")
    void softDeleteKid(@Param("kidId") Long kidId);

    @Modifying
    @Query("UPDATE KidMbti km SET km.isDeleted = true WHERE km.kid.id = :kidId")
    void softDeleteKidMbti(@Param("kidId") Long kidId);

    @Modifying
    @Query("UPDATE KidMbtiHistory kmh SET kmh.isDeleted = true WHERE kmh.kid.id = :kidId")
    void softDeleteKidMbtiHistory(@Param("kidId") Long kidId);

    @Modifying
    @Query("UPDATE MbtiAnswer ma SET ma.isDeleted = true WHERE ma.kid.id = :kidId")
    void softDeleteMbtiAnswer(@Param("kidId") Long kidId);

    @Modifying
    @Query("UPDATE KidBadgeAwarded kba SET kba.isDeleted = true WHERE kba.kid.id = :kidId")
    void softDeleteKidBadgeAwarded(@Param("kidId") Long kidId);


}