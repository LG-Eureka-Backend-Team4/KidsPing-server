package com.kidsworld.kidsping.domain.user.dto.response;

import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetUserResponse {

    private long userId;
    private String userName;
    private String phone;
    private String email;
    private Role role;
    private List<GetKidListResponse> kids;

}