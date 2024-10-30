package com.kidsworld.kidsping.domain.kid.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class KidLevelAndBadgesResponse {
    private int level;
    private List<KidBadgeAwardedResponse> badges;

    public KidLevelAndBadgesResponse(int level, List<KidBadgeAwardedResponse> badges) {
        this.level = level;
        this.badges = badges;
    }
}
