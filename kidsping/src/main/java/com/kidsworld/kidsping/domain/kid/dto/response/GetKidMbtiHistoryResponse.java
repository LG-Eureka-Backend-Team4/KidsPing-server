package com.kidsworld.kidsping.domain.kid.dto.response;

import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetKidMbtiHistoryResponse {
    private Long kidMbtiHistoryId;
    private Long kidId;
    private String mbtiStatus;
    private String createdAt;

    public GetKidMbtiHistoryResponse(KidMbtiHistory history) {
        this.kidMbtiHistoryId = history.getId();
        this.kidId = history.getKid().getId();
        this.mbtiStatus = history.getMbtiStatus().name();
        this.createdAt = history.getCreatedAt().toString();
    }
}
