package com.kidsworld.kidsping.domain.mbti.controller;

import com.kidsworld.kidsping.domain.mbti.dto.response.GetMbtiInfoResponse;
import com.kidsworld.kidsping.domain.mbti.service.MbtiInfoService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/mbtiinfo")
@RequiredArgsConstructor
public class MbtiInfoController {

    private final MbtiInfoService mbtiInfoService;

    /*
     * 자녀 성향 조회
     */
    @GetMapping("/{mbtiInfoId}")
    public ResponseEntity<ApiResponse<GetMbtiInfoResponse>> getMbtiInfo(@PathVariable Long mbtiInfoId) {
        GetMbtiInfoResponse response = mbtiInfoService.getMbtiInfo(mbtiInfoId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "MBTI 정보를 성공적으로 조회했습니다.");
    }

}
