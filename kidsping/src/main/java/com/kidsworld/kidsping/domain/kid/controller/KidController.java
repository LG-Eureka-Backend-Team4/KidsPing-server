package com.kidsworld.kidsping.domain.kid.controller;

import com.kidsworld.kidsping.domain.kid.dto.request.CreateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.request.UpdateKidRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.*;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.service.UserService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    /*
    자녀 프로필 생성
    */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CreateKidResponse>> createKid(@RequestBody CreateKidRequest request) {
        CreateKidResponse response = kidService.createKid(request);
        return ApiResponse.created("/api/kids/" + response.getKidId(),
                ExceptionCode.CREATED.getCode(), response, ExceptionCode.CREATED.getMessage());
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
    @PutMapping("/{kidId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UpdateKidResponse>> updateKid(@PathVariable Long kidId,
                                                                    @RequestBody UpdateKidRequest request) {
        UpdateKidResponse response = kidService.updateKid(kidId, request);
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


    /*
    회원 자녀 리스트 조회
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GetKidListResponse>>> getKidList(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        List<GetKidListResponse> response = kidService.getKidsList(user.getId());
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 목록을 성공적으로 조회했습니다."
        );
    }
}