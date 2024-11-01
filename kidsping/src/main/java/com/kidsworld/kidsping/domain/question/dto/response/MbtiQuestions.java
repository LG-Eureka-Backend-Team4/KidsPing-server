package com.kidsworld.kidsping.domain.question.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MbtiQuestions {

    private List<MbtiQuestionResponse> mbtiQuestionResponses = new ArrayList<>();

    public MbtiQuestions(List<MbtiQuestionResponse> mbtiQuestionResponses) {
        this.mbtiQuestionResponses = mbtiQuestionResponses;
    }
}
