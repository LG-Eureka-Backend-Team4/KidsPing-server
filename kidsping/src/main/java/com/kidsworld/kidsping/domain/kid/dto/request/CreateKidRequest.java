package com.kidsworld.kidsping.domain.kid.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateKidRequest {
    private Long userId;
    private String kidName;
    private String gender;
    private String birth;
}