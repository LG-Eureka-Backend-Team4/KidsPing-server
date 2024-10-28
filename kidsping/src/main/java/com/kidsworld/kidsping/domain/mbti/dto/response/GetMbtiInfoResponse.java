package com.kidsworld.kidsping.domain.mbti.dto.response;

import com.kidsworld.kidsping.domain.mbti.entity.MbtiInfo;
import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetMbtiInfoResponse {

    private Long id;
    private String mbtiStatus;
    private String title;
    private RoleModel roleModel;
    private String description;
    private String imageUrl;

    public static GetMbtiInfoResponse from(MbtiInfo mbtiInfo) {
        return GetMbtiInfoResponse.builder()
                .id(mbtiInfo.getId())
                .mbtiStatus(mbtiInfo.getMbtiStatus())
                .title(mbtiInfo.getTitle())
                .roleModel(mbtiInfo.getRoleModel())
                .description(mbtiInfo.getDescription())
                .imageUrl(mbtiInfo.getUploadedFile().getFileUrl())
                .build();
    }
}