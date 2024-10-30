package com.kidsworld.kidsping.domain.kid.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KidBadgeAwardedRequest {
    private String badgeName;
    private String description;
    private String imageUrl;

}
