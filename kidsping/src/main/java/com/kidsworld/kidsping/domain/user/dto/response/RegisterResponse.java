package com.kidsworld.kidsping.domain.user.dto.response;

import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String email;
    private Role role;
}
