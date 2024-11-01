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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }

        return userRepository.save(registerRequest.toEntity());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    //순수하게 자녀 목록만 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetKidListResponse> getKidsList(Long userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(GetKidListResponse::from)
                .collect(Collectors.toList());
    }


    //Api를 위한 메서드 (User ID + 자녀 목록 반환)
    @Override
    @Transactional(readOnly = true)
    public List<Object> getUserKidsList(Long userId, String userEmail) {
        User user = findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        List<GetKidListResponse> kidsList = getKidsList(user.getId());

        if (kidsList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Object> responseData = new ArrayList<>();
        responseData.add(Collections.singletonMap("userId", user.getId()));
        responseData.addAll(kidsList);

        return responseData;
    }


    @Override
    public User save(RegisterRequest registerRequest) { return userRepository.save(registerRequest.toEntity());}

    //기존 유저 정보 수정
    @Override
    public User update(User user) { return userRepository.save(user);}

    //소셜 ID로 사용자를 조회
    @Override
    public Optional<User> findBySocialId(String socialId) { return userRepository.findBySocialId(socialId);}


    // 일반 회원 리프레시 토큰 발급
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
