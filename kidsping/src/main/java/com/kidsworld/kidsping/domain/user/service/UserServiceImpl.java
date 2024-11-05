package com.kidsworld.kidsping.domain.user.service;

import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.DuplicateEmailException;
import com.kidsworld.kidsping.domain.user.exception.SocialLoginNotAllowedException;
import com.kidsworld.kidsping.domain.user.exception.UnauthorizedUserException;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입, 이메일 중복 확인 후 새로운 사용자 등록
     */
    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }

        return userRepository.save(registerRequest.toEntity());
    }

    /**
     * 이메일을 통한 사용자 조회 메서드
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    /**
     * Spring Security에서 사용자를 로드하는 메서드 (이메일 기준)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    /**
     * 자녀 목록 조회 (해당 사용자와 논리적 삭제되지 않은 자녀들)
     * 자녀 목록을 조회하는 데 있어 재사용성이 높고, 자녀 목록을 필요로 하는 다른 메서드에서도 사용
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetKidListResponse> getKidsList(Long userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(GetKidListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 자녀 목록 조회 (해당 사용자와 논리적 삭제되지 않은 자녀들만)
     *  Api를 위한 메서드
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserKidsListNoAuth(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<GetKidListResponse> kidsList = getKidsList(user.getId());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", user.getId());
        responseData.put("kids", kidsList);

        return responseData;
    }


    /**
     * 사용자 정보 저장(회원가입 시 사용)
     */
    @Override
    public User save(RegisterRequest registerRequest) { return userRepository.save(registerRequest.toEntity());}


    /**
     * 기존 사용자 정보 수정
     */
    //기존 유저 정보 수정
    @Override
    public User update(User user) { return userRepository.save(user);}

    /**
     * 소셜 ID로 사용자 조회 ( 특정 소셜 ID를 가진 사용자를 조회 )
     */
    @Override
    public Optional<User> findBySocialId(String socialId) { return userRepository.findBySocialId(socialId);}


    /**
     * 일반 회원 리프레시 토큰
     */
    @Override
    @Transactional
    public LoginResponse refreshNormalUserToken(String email) {
        User user = findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (user.getSocialId() != null) {
            throw new SocialLoginNotAllowedException();
        }
        if (user.getRefreshToken() == null) {
            throw new UnauthorizedUserException();
        }

        // 토큰에 비밀번호는 비어있게 했음
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), "", new ArrayList<>()
        );

        String newAccessToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        user.updateRefreshToken(newRefreshToken);
        update(user);

        List<GetKidListResponse> kidsList = getKidsList(user.getId());

        return LoginResponse.of(user.getEmail(), newAccessToken, newRefreshToken, user.getId(), kidsList
        );
    }


}
