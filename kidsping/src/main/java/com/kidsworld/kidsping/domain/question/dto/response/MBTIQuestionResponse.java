package com.kidsworld.kidsping.domain.question.dto.response;

import com.kidsworld.kidsping.domain.mbti.entity.enums.PersonalityTrait;
import com.kidsworld.kidsping.domain.question.entity.MBTIQuestion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MBTIQuestionResponse {

    private Long id;
    private String content;
    private PersonalityTrait personalityTrait;
    private boolean isDeleted;

    @Builder
    private MBTIQuestionResponse(Long id, String content, PersonalityTrait personalityTrait, boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.personalityTrait = personalityTrait;
        this.isDeleted = isDeleted;
    }

    public static MBTIQuestionResponse from(MBTIQuestion mbtiQuestion) {
        return MBTIQuestionResponse.builder()
                .id(mbtiQuestion.getId())
                .content(mbtiQuestion.getContent())
                .personalityTrait(mbtiQuestion.getPersonalityTrait())
                .isDeleted(mbtiQuestion.getIsDeleted())
                .build();
    }
}
