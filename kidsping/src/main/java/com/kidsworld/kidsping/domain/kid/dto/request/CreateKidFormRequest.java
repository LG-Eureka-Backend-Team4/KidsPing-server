package com.kidsworld.kidsping.domain.kid.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateKidFormRequest {
    private Long userId;
    private String kidName;
    private String gender;
    private String birth;
    private MultipartFile profileImage;

    // CreateKidRequest로 변환하는 메서드
    public CreateKidRequest toCreateKidRequest() {
        return CreateKidRequest.builder()
                .userId(this.userId)
                .kidName(this.kidName)
                .gender(this.gender)
                .birth(this.birth)
                .build();
    }
}