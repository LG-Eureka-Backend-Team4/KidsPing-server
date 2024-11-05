package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.repository.KidMbtiRepository;
import com.kidsworld.kidsping.domain.kid.service.KidMbtiService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KidMbtiServiceImpl implements KidMbtiService {

    private final KidMbtiRepository kidMbtiRepository;

    @Override
    public void deleteExpiredKidMbti() {
        kidMbtiRepository.deleteExpiredKidMbti(LocalDateTime.now());
    }
}