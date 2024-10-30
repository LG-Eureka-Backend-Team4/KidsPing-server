package com.kidsworld.kidsping.domain.kid.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class KidBadgeAwardedResponse {
    private String badgeName;
    private String description;
    private String imageUrl;

    public KidBadgeAwardedResponse(String badgeName, String description, String imageUrl) {
        this.badgeName = badgeName;
        this.description = description;
        this.imageUrl = imageUrl;
    }

}