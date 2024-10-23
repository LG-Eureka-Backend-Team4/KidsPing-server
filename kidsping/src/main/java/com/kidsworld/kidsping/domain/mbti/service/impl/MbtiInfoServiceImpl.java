package com.kidsworld.kidsping.domain.mbti.service.impl;


import com.kidsworld.kidsping.domain.mbti.dto.response.GetMbtiInfoResponse;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiInfo;
import com.kidsworld.kidsping.domain.mbti.exception.NotFoundMbtiInfoException;
import com.kidsworld.kidsping.domain.mbti.repository.MbtiInfoRepository;
import com.kidsworld.kidsping.domain.mbti.service.MbtiInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MbtiInfoServiceImpl implements MbtiInfoService {


    private final MbtiInfoRepository mbtiInfoRepository;

    @Override
    @Transactional
    public GetMbtiInfoResponse getMbtiInfo(Long mbtiInfoId) {
        MbtiInfo mbtiInfo = mbtiInfoRepository.findById(mbtiInfoId)
                .orElseThrow(() -> new NotFoundMbtiInfoException("MBTI 정보가 존재하지 않습니다: " + mbtiInfoId));

        return GetMbtiInfoResponse.from(mbtiInfo);
    }

}