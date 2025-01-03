package com.kidsworld.kidsping.domain.question.dto.response;

import com.kidsworld.kidsping.domain.question.entity.MbtiQuestion;
import com.kidsworld.kidsping.global.common.enums.PersonalityTrait;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MbtiQuestionResponse {

    private Long id;
    private String content;
    private PersonalityTrait personalityTrait;
    private boolean isDeleted;

    @Builder
    private MbtiQuestionResponse(Long id, String content, PersonalityTrait personalityTrait, boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.personalityTrait = personalityTrait;
        this.isDeleted = isDeleted;
    }

    public static MbtiQuestionResponse from(MbtiQuestion mbtiQuestion) {
        return MbtiQuestionResponse.builder()
                .id(mbtiQuestion.getId())
                .content(mbtiQuestion.getContent())
                .personalityTrait(mbtiQuestion.getPersonalityTrait())
                .isDeleted(mbtiQuestion.getIsDeleted())
                .build();
    }
}
