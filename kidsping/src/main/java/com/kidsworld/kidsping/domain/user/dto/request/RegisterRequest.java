package com.kidsworld.kidsping.domain.user.dto.request;

import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest implements Serializable {
    private String email;
    private String password;
    private String username;
    private String phone;
    private Role role;

    public User toEntity() {
        return User.builder().email(email).userName(username).password(password).role(role).build();
    }
}