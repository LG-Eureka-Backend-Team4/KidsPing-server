package com.kidsworld.kidsping.domain.user.dto.response;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class GetKidListResponse {
    private Long kidId;
    private String name;
    private String profileUrl;

    public static GetKidListResponse from(Kid kid) {
        return GetKidListResponse.builder()
                .kidId(kid.getId())
                .name(kid.getName())
                .profileUrl(kid.getUploadedFile() != null ? kid.getUploadedFile().getFileUrl() : null)
                .build();
    }
}

