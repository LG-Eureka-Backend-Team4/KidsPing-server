package com.kidsworld.kidsping.domain.kid.service;

import java.util.List;

public interface KidMbtiService {

    List<Long> findExpiredKidMbtiIds();

    void deleteExpiredKidMbti(List<Long> expiredKidMbtiIds);
}