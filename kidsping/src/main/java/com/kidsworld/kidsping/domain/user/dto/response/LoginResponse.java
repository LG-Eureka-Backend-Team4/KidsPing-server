package com.kidsworld.kidsping.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private final String jwt;
    private Long userId;
    private List<GetKidListResponse> kidsList;

}
