package com.kidsworld.kidsping.domain.kid.controller;

import com.kidsworld.kidsping.domain.kid.dto.request.KidCreateRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMBTIDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidUpdateRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.KidCreateResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidDeleteResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidGetResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.KidUpdateResponse;
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


@RestController
@RequestMapping("/api/kids")
@RequiredArgsConstructor
public class KidController {

    private final KidService kidService;

    /*
    자녀 프로필 생성
    */
    @PostMapping
    public ResponseEntity<ApiResponse<KidCreateResponse>> createKid(@RequestBody KidCreateRequest request) {
        KidCreateResponse response = kidService.createKid(request);
        return ApiResponse.created("/api/kids/" + response.getKidId(), ExceptionCode.OK.getCode(), response,
                "자녀 프로필이 성공적으로 추가되었습니다.");
    }


    /*
    자녀 프로필 조회
    */
    @GetMapping("/{kidId}")
    public ResponseEntity<ApiResponse<KidGetResponse>> getKid(@PathVariable Long kidId) {
        KidGetResponse response = kidService.getKid(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 조회했습니다.");
    }

    /*
    자녀 프로필 수정
    */
    @PutMapping("/{kidId}")
    public ResponseEntity<ApiResponse<KidUpdateResponse>> updateKid(@PathVariable Long kidId,
                                                                    @RequestBody KidUpdateRequest request) {
        KidUpdateResponse response = kidService.updateKid(kidId, request);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 수정했습니다.");
    }

    /*
    자녀 프로필 삭제
     */
    @DeleteMapping("/{kidId}")
    public ResponseEntity<ApiResponse<KidDeleteResponse>> deleteKid(@PathVariable Long kidId) {
        KidDeleteResponse response = kidService.deleteKid(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 삭제했습니다.");
    }

    /*
     * 자녀 성향 진단
     * */
    @PostMapping("/mbti/diagonosis")
    public void diagnoseKidMBTI(@RequestBody KidMBTIDiagnosisRequest diagnosisRequest) {
        kidService.diagnoseKidMBTI(diagnosisRequest);
    }
}