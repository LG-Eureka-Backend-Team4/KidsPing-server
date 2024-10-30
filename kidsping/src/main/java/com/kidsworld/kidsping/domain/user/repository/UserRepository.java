package com.kidsworld.kidsping.domain.user.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    //User의 kid리스트
    @Query("select k from Kid k where k.user.id = :userId and k.isDeleted = false")
    List<Kid> findByUserIdAndIsDeletedFalse(@Param("userId") Long userId);
}
