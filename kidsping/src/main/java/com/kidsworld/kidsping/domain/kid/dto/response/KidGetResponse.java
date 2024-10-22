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
public class KidGetResponse {
    private Long kidId;
    private Long userId;
    private String kidName;
    private String gender;
    private String birth;

    public KidGetResponse(Kid kid) {
        this.kidId = kid.getId();
        this.userId = kid.getUser().getId();
        this.kidName = kid.getName();
        this.gender = kid.getGender().name();
        this.birth = kid.getBirth().toString();
    }
}