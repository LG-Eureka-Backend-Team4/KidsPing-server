package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.repository.KidMbtiRepository;
import com.kidsworld.kidsping.domain.kid.service.KidMbtiService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KidMbtiServiceImpl implements KidMbtiService {

    private final KidMbtiRepository kidMbtiRepository;

    /*
     * isDeleted 값이 true 이면서 날짜가 현재로부터 한달이 지난 id 값을 조회하는 메서드
     * */
    @Override
    public List<Long> findExpiredKidMbtiIds() {
        return kidMbtiRepository.findExpiredKidMbtiIds(LocalDateTime.now());
    }

    @Override
    public void deleteExpiredKidMbti(List<Long> expiredKidMbtiIds) {
        kidMbtiRepository.deleteExpiredKidMbti(expiredKidMbtiIds);
    }
}