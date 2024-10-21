package com.kidsworld.kidsping.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
    private String email;
    private final String jwt;
}
