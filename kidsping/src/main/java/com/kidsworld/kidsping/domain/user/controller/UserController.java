package com.kidsworld.kidsping.domain.user.controller;

import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.request.LoginRequest;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.dto.response.GetUserResponse;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.dto.response.RegisterResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /*
    회원가입
    */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = userService.save(registerRequest);
        return ResponseEntity.ok(RegisterResponse.builder().id(user.getId()).email(user.getEmail()).role(user.getRole()).build());
    }


    /*
    로그인
    */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        List<GetKidListResponse> kidsList = userService.getKidsList(user.getId());

        return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), jwt, user.getId(), kidsList));
    }


    /*
    로그아웃
    */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserDetails userDetails) {

        userService.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return ApiResponse.ok(ExceptionCode.OK.getCode(), null, "로그아웃 되었습니다.");
    }



    /*
    회원정보조회
    */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetUserResponse>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.unAuthorized(
                    ExceptionCode.UNAUTHORIZED_USER.getCode(),
                    ExceptionCode.UNAUTHORIZED_USER.getMessage()
            );
        }

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        GetUserResponse response = GetUserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }


    /*
    회원 자녀 리스트 조회
     */
    @GetMapping("/{userId}/kidslist")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GetKidListResponse>>> getKidList(@PathVariable("userId") Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        List<GetKidListResponse> kidsList = userService.getKidsList(user.getId());

        if (kidsList.isEmpty()) {
            return ApiResponse.ok(ExceptionCode.OK.getCode(), null, "등록된 자녀가 없습니다. 자녀를 등록해주세요.");
        }

        List<GetKidListResponse> response = userService.getKidsList(user.getId());
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "자녀 목록을 성공적으로 조회했습니다.");
    }




}
