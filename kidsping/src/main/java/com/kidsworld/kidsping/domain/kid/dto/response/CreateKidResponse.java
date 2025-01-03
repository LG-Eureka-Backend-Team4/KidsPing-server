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
public class CreateKidResponse {
    private Long kidId;
    private Long userId;
    private String kidName;
    private String gender;
    private String birth;
    private String fileUrl;

    public static CreateKidResponse from(Kid kid) {
        return CreateKidResponse.builder()
                .kidId(kid.getId())
                .userId(kid.getUser().getId())
                .kidName(kid.getName())
                .gender(kid.getGender().name())
                .birth(kid.getBirth().toString())
                .fileUrl(kid.getUploadedFile() != null ? kid.getUploadedFile().getFileUrl() : null)
                .build();
    }
}