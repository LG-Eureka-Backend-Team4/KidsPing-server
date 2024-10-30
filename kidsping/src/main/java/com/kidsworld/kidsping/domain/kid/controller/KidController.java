package com.kidsworld.kidsping.domain.kid.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.*;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/kids")
@RequiredArgsConstructor
public class KidController {

    private final KidService kidService;

    /*
    자녀 프로필 생성
    */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreateKidResponse>> createKid(
            @RequestPart String request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        CreateKidResponse response = kidService.createKid(request, profileImage);
        return ApiResponse.created("/api/kids/" + response.getKidId(), ExceptionCode.CREATED.getCode(), response, ExceptionCode.CREATED.getMessage());
    }


    /*
    자녀 프로필 조회
    */
    @GetMapping("/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<GetKidResponse>> getKid(@PathVariable Long kidId) {
        GetKidResponse response = kidService.getKid(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 조회했습니다.");
    }

    /*
    자녀 프로필 수정
    */
    @PutMapping(value = "/{kidId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UpdateKidResponse>> updateKid(
            @PathVariable Long kidId,
            @RequestPart String request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        UpdateKidRequest kidRequest = mapper.readValue(request, UpdateKidRequest.class);

        UpdateKidResponse response = kidService.updateKid(kidId, kidRequest, profileImage);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 수정했습니다.");
    }

    /*
    자녀 프로필 삭제
    */
    @DeleteMapping("/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<DeleteKidResponse>> deleteKid(@PathVariable Long kidId) {
        DeleteKidResponse response = kidService.deleteKid(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 프로필을 성공적으로 삭제했습니다.");
    }

    /*
    자녀 성향 진단
    */
    @PostMapping("/mbti/diagonosis")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void diagnoseKidMbti(@RequestBody KidMbtiDiagnosisRequest diagnosisRequest) {
        kidService.diagnoseKidMbti(diagnosisRequest);
    }

    /*
    자녀 성향 히스토리 조회
    */
    @GetMapping("/{kidId}/mbtihistory")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GetKidMbtiHistoryResponse>>> getKidMbtiHistory(@PathVariable Long kidId) {
        List<GetKidMbtiHistoryResponse> response = kidService.getKidMbtiHistory(kidId);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 히스토리를 성공적으로 조회했습니다.");
    }


}