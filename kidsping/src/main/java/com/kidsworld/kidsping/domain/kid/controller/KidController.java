package com.kidsworld.kidsping.domain.kid.controller;

import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.*;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/kids")
@RequiredArgsConstructor
public class KidController {

    private final KidService kidService;

    /*
    자녀 프로필 생성
    */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateKidResponse>> createKid(@RequestBody CreateKidRequest request) {
        CreateKidResponse response = kidService.createKid(request);
        return ApiResponse.created("/api/kids/" + response.getKidId(), ExceptionCode.OK.getCode(), response,
                "자녀 프로필이 성공적으로 추가되었습니다.");
    }


    /*
    자녀 프로필 조회
    */
    @GetMapping("/{kidId}")
    public ResponseEntity<ApiResponse<GetKidResponse>> getKid(@PathVariable Long kidId) {
        GetKidResponse response = kidService.getKid(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 조회했습니다.");
    }

    /*
    자녀 프로필 수정
    */
    @PutMapping("/{kidId}")
    public ResponseEntity<ApiResponse<UpdateKidResponse>> updateKid(@PathVariable Long kidId,
                                                                    @RequestBody UpdateKidRequest request) {
        UpdateKidResponse response = kidService.updateKid(kidId, request);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 수정했습니다.");
    }

    /*
    자녀 프로필 삭제
    */
    @DeleteMapping("/{kidId}")
    public ResponseEntity<ApiResponse<DeleteKidResponse>> deleteKid(@PathVariable Long kidId) {
        DeleteKidResponse response = kidService.deleteKid(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 삭제했습니다.");
    }

    /*
    자녀 성향 진단
    */
    @PostMapping("/mbti/diagonosis")
    public void diagnoseKidMbti(@RequestBody KidMbtiDiagnosisRequest diagnosisRequest) {
        kidService.diagnoseKidMbti(diagnosisRequest);
    }

    /*
    자녀 성향 히스토리 조회
    */
    @GetMapping("/{kidId}/mbtihistory")
    public ResponseEntity<ApiResponse<List<GetKidMbtiHistoryResponse>>> getKidMbtiHistory(@PathVariable Long kidId) {
        List<GetKidMbtiHistoryResponse> response = kidService.getKidMbtiHistory(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 히스토리를 성공적으로 조회했습니다.");
    }


}