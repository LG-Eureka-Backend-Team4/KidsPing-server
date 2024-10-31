package com.kidsworld.kidsping.domain.user.service;

import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User registerUser(RegisterRequest registerRequest);
    Optional<User> findByEmail(String email);

    //회원의 자녀 리스트 조회
    List<GetKidListResponse> getKidsList(Long userId);

    User save(RegisterRequest registerRequest);
    User update(User user);
    Optional<User> findBySocialId(String socialId);
}