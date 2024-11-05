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
    private final String refreshToken;
    private Long userId;
    private List<GetKidListResponse> data;

    public static LoginResponse of(String email, String jwt,String refreshToken ,Long userId, List<GetKidListResponse> kidsList) {
        return LoginResponse.builder()
                .email(email)
                .jwt(jwt)
                .refreshToken(refreshToken)
                .userId(userId)
                .data(kidsList)
                .build();
    }

}
