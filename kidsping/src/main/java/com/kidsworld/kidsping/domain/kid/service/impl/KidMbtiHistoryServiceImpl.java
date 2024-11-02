package com.kidsworld.kidsping.domain.kid.service.impl;

import com.kidsworld.kidsping.domain.kid.repository.KidMbtiHistoryRepository;
import com.kidsworld.kidsping.domain.kid.service.KidMbtiHistoryService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KidMbtiHistoryServiceImpl implements KidMbtiHistoryService {

    private final KidMbtiHistoryRepository kidMbtiHistoryRepository;

    @Override
    public void deleteExpiredKidMbtiHistory() {
        kidMbtiHistoryRepository.deleteExpiredKidMbtiHistory(LocalDateTime.now());
    }
}
