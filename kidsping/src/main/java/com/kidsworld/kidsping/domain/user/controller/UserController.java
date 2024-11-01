package com.kidsworld.kidsping.domain.user.controller;

import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.request.LoginRequest;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.dto.response.GetUserResponse;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.dto.response.RegisterResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.service.KakaoService;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
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
    private final KakaoService kakaoService;

    /*
    회원가입
    */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodedPassword);

        User user = userService.registerUser(registerRequest);
        RegisterResponse response = RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "회원가입에 성공했습니다.");
    }

    /*
    로그인
    */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);


        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        user.updateRefreshToken(refreshToken);
        userService.update(user);

        List<GetKidListResponse> kidsList = userService.getKidsList(user.getId());

        return ApiResponse.ok(ExceptionCode.OK.getCode(), new LoginResponse(userDetails.getUsername(), jwt, refreshToken, user.getId(), kidsList), "로그인에 성공했습니다.");
    }


    /*
    로그아웃
    */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        user.removeRefreshToken();
        userService.update(user);

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
                .orElseThrow(UserNotFoundException::new);

        List<GetKidListResponse> kidsList = userService.getKidsList(user.getId());

        GetUserResponse response = GetUserResponse.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .kids(kidsList)
                .build();

        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }


    /*
     회원 자녀 리스트 조회
     */
//    @GetMapping("/{userId}/kids/list")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    public ResponseEntity<ApiResponse<List<Object>>> getKidList(@PathVariable("userId") Long userId,
//                                                                @AuthenticationPrincipal UserDetails userDetails) {
//        List<Object> responseData = userService.getUserKidsList(userId, userDetails.getUsername());
//
//        return ApiResponse.ok(ExceptionCode.OK.getCode(), responseData, "자녀 목록을 성공적으로 조회했습니다.");
//    }
    // 박종혁씨가 인증 풀어달라고 해서 풀어준 거
    @GetMapping("/{userId}/kids/list")
    public ResponseEntity<ApiResponse<List<Object>>> getKidList(@PathVariable("userId") Long userId) {
        List<Object> responseData = userService.getUserKidsListNoAuth(userId);  // 새로운 서비스 메서드 호출
        return ApiResponse.ok(ExceptionCode.OK.getCode(), responseData, "자녀 목록을 성공적으로 조회했습니다.");
    }




    /*
    카카오 로그인
    */
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> kakaoLogin(@RequestParam("code") String code, HttpServletResponse httpServletResponse) {
        LoginResponse response = kakaoService.handleKakaoLogin(code);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "카카오 로그인에 성공했습니다.");
    }


    /*
    카카오 로그아웃
    */
    @PostMapping("/logout/kakao")
    public ResponseEntity<ApiResponse<Void>> kakaoLogout(@RequestHeader("Authorization") String bearerToken,
                                                         @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (user.getSocialId() != null) { //카카오 계정인지 검증
            String accessToken = bearerToken.substring(7);
            kakaoService.kakaoLogout(accessToken, userDetails.getUsername());
        }

        return ApiResponse.ok(ExceptionCode.OK.getCode(), null, "카카오 로그아웃 되었습니다.");
    }


    /*
    일반회원 리프레시 토큰
    */
    @PostMapping("/refresh/token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshNormalToken(
            @AuthenticationPrincipal UserDetails userDetails) {
        LoginResponse response = userService.refreshNormalUserToken(userDetails.getUsername());

        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "토큰이 갱신되었습니다.");
    }



    /*
    카카오 리프레시 토큰
     */
    @PostMapping("/refresh/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(UserNotFoundException::new);

            // 일반 로그인 사용자의 접근 차단
            if (user.getSocialId() == null) {
                return ApiResponse.forbidden(ExceptionCode.GENERAL_LOGIN_NOT_ALLOWED.getCode(), "일반 로그인 사용자는 이 엔드포인트를 사용할 수 없습니다.");
            }

            LoginResponse response = kakaoService.refreshUserToken(userDetails.getUsername());
            return ApiResponse.ok(ExceptionCode.OK.getCode(), response, "토큰이 갱신되었습니다.");


        } catch (RuntimeException e) {
            if (e.getMessage().equals("refresh_token_expired")) {
                return ApiResponse.unAuthorized(ExceptionCode.UNAUTHORIZED_USER.getCode(), "재로그인이 필요합니다."
                );
            }
            throw e;
        }
    }


}
