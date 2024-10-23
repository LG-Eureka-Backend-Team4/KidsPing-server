package com.kidsworld.kidsping.domain.mbti.service;


import com.kidsworld.kidsping.domain.mbti.dto.response.GetMbtiInfoResponse;

public interface MbtiInfoService {
    GetMbtiInfoResponse getMbtiInfo(Long kidId);
}
