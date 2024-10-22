package com.kidsworld.kidsping.domain.kid.controller;

import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.CreateKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.DeleteKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.UpdateKidResponse;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        return ApiResponse.created("/api/kids/" + response.getKidId(), ExceptionCode.OK.getCode(), response, "자녀 프로필이 성공적으로 추가되었습니다.");
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
    public ResponseEntity<ApiResponse<UpdateKidResponse>> updateKid(@PathVariable Long kidId, @RequestBody UpdateKidRequest request) {
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




    @PostMapping("/mbti/diagonosis")
    public void diagnoseKidMBTI(@RequestBody KidMBTIDiagnosisRequest diagnosisRequest) {
        kidService.diagnoseKidMBTI(diagnosisRequest);
    }
}