package com.kidsworld.kidsping.domain.user.service;


import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User registerUser(RegisterRequest registerRequest);

    Optional<User> findByEmail(String email);

    List<GetKidListResponse> getKidsList(Long userId);

    //List<Object> getUserKidsList(Long userId, String userEmail);

    Map<String, Object> getUserKidsListNoAuth(Long userId);

    Optional<User> findBySocialId(String socialId);

    User save(RegisterRequest registerRequest);

    User update(User user);

    LoginResponse refreshNormalUserToken(String email);

}