package com.kidsworld.kidsping.domain.kid.service.impl;


import com.kidsworld.kidsping.domain.kid.dto.request.KidCreateRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidUpdateRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.KidCreateResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidDeleteResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidGetResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidUpdateResponse;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class KidServiceImpl implements KidService {

    private final KidRepository kidRepository;
    private final UserRepository userRepository;


    /*
    자녀 프로필 생성
    */
    @Override
    @Transactional
    public KidCreateResponse createKid(KidCreateRequest request) {
        // 사용자의 자녀 수 확인
        long kidCount = kidRepository.countByUserId(request.getUserId());
        if (kidCount >= 5) {
            throw new IllegalStateException("최대 5명의 자녀만 등록할 수 있습니다.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        // 현재 사용 중인 ID들을 가져와서 빈 번호 찾기
        List<Long> existingIds = kidRepository.findKidIdsByUserId(request.getUserId());
        Long nextId = 1L;

        // 1부터 순서대로 확인하면서 비어있는 첫 번호 찾기
        while (existingIds.contains(nextId)) {
            nextId++;
        }

        Kid kid = Kid.builder()
                .id(nextId)
                .gender(Gender.valueOf(request.getGender()))
                .name(request.getKidName())
                .birth(LocalDate.parse(request.getBirth()))
                .isDeleted(false)
                .user(user)
                .build();

        Kid savedKid = kidRepository.save(kid);
        return KidCreateResponse.from(savedKid);
    }




    /*
    자녀 프로필 조회
    */
    @Override
    @Transactional
    public KidGetResponse getKid(Long kidId) {
        Kid kid = findKidOrThrow(kidId);
        return new KidGetResponse(kid);
    }


    /*
    자녀 프로필 수정
    */
    @Override
    @Transactional
    public KidUpdateResponse updateKid(Long kidId, KidUpdateRequest request) {
        Kid kid = findKidOrThrow(kidId);

        kid.update(
                Gender.valueOf(request.getGender()),
                request.getKidName(),
                LocalDate.parse(request.getBirth())
        );

        return KidUpdateResponse.from(kid);
    }


    /*
    자녀 프로필 삭제
    */
    @Override
    @Transactional
    public KidDeleteResponse deleteKid(Long kidId) {
        Kid kid = findKidOrThrow(kidId);
        kidRepository.delete(kid);
        return new KidDeleteResponse(kidId);
    }



    private Kid findKidOrThrow(Long kidId) {
        return kidRepository.findById(kidId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 자녀를 찾을 수 없습니다: " + kidId));
    }

}
