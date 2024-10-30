package com.kidsworld.kidsping.domain.kid.dto.response;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKidResponse {
    private Long kidId;
    private Long userId;
    private String kidName;
    private String gender;
    private String birth;
    private String profileImageUrl;

    public static UpdateKidResponse from(Kid kid) {
        return UpdateKidResponse.builder()
                .kidId(kid.getId())
                .userId(kid.getUser().getId())
                .kidName(kid.getName())
                .gender(kid.getGender().name())
                .birth(kid.getBirth().toString())
                .profileImageUrl(kid.getUploadedFile().getFileUrl())
                .build();
    }
}