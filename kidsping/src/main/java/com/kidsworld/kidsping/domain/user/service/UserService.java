package com.kidsworld.kidsping.domain.user.service;

import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(RegisterRequest registerRequest);
}