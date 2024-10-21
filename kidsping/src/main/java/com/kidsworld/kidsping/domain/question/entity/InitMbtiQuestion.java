package com.kidsworld.kidsping.domain.question.entity;

import com.kidsworld.kidsping.domain.mbti.entity.enums.PersonalityTrait;
import com.kidsworld.kidsping.domain.question.repository.MbtiQuestionRepository;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitMbtiQuestion {

    @Autowired
    private MbtiQuestionRepository mbtiQuestionRepository;

    @PostConstruct
    public void init() {
        MbtiQuestion[] questions = {
                // Extraversion (E)
                new MbtiQuestion("놀이시간이 되면 친구들과 어울려 활동하는 것을 즐긴다.", PersonalityTrait.EXTRAVERSION, false),
                new MbtiQuestion("새로운 환경에서도 자신있게 다른 친구들과 대화한다.", PersonalityTrait.EXTRAVERSION, false),
                new MbtiQuestion("여러 사람과 함께 있는 상황에서 에너지를 얻는 것 같다.", PersonalityTrait.EXTRAVERSION, false),

                // Introversion (I)
                new MbtiQuestion("혼자만의 시간을 가지며 책을 읽거나 조용히 장난감을 가지고 노는 것을 선호한다.", PersonalityTrait.INTROVERSION, false),
                new MbtiQuestion("여러 명과 함께 있을 때보다 혼자 또는 소수의 친구와 함께 있을 때 더 편안해 보인다.", PersonalityTrait.INTROVERSION,
                        false),
                new MbtiQuestion("새로운 환경에서 조용히 관찰하며 서서히 적응해 나가는 것 같다.", PersonalityTrait.INTROVERSION, false),

                // Sensing (S)
                new MbtiQuestion("사소한 디테일을 잘 기억하고 반복되는 일상에 익숙한 편이다.", PersonalityTrait.SENSING, false),
                new MbtiQuestion("친구들이 말을 할 때 주로 사실과 구체적인 정보를 듣는 데 관심이 있다.", PersonalityTrait.SENSING, false),
                new MbtiQuestion("지시사항이나 규칙을 잘 따르고, 이를 중요하게 여긴다.", PersonalityTrait.SENSING, false),

                // Intuition (N)
                new MbtiQuestion("상황이나 사건을 접할 때 추상적으로 상상하거나 창의적인 아이디어를 자주 떠올린다.", PersonalityTrait.INTUITION, false),
                new MbtiQuestion("일상적인 일에서 새로운 방식이나 독창적인 접근을 즐기고 시도한다.", PersonalityTrait.INTUITION, false),
                new MbtiQuestion("이야기나 설명을 들을 때 구체적인 사실보다 전체적인 의미와 맥락에 집중한다.", PersonalityTrait.INTUITION, false),

                // Thinking (T)
                new MbtiQuestion("상황을 이해할 때 논리적으로 접근하고, 옳고 그름을 따지려 한다.", PersonalityTrait.THINKING, false),
                new MbtiQuestion("친구와의 갈등 상황에서 문제 해결에 더 집중하는 경향이 있다.", PersonalityTrait.THINKING, false),
                new MbtiQuestion("스스로의 선택이나 행동을 할 때 감정보다는 사실과 논리를 우선한다.", PersonalityTrait.THINKING, false),

                // Feeling (F)
                new MbtiQuestion("다른 사람의 감정을 잘 이해하고, 친구가 어려움을 겪을 때 공감하며 위로해준다.", PersonalityTrait.FEELING, false),
                new MbtiQuestion("어려운 상황에서 사람들의 기분이나 감정을 고려하여 행동하려는 경향이 있다.", PersonalityTrait.FEELING, false),
                new MbtiQuestion("친구들과 의견이 다를 때 타인의 의견을 존중하고 조율하려고 노력한다.", PersonalityTrait.FEELING, false),

                // Judging (J)
                new MbtiQuestion("일의 순서와 계획을 세워 미리 준비하고, 계획대로 실행하려고 한다.", PersonalityTrait.JUDGING, false),
                new MbtiQuestion("놀이 시간 동안 정해진 규칙을 지키려고 하고, 변화를 싫어하는 편이다.", PersonalityTrait.JUDGING, false),
                new MbtiQuestion("숙제를 할 때, 언제 끝낼지 계획을 세우고 체계적으로 접근한다.", PersonalityTrait.JUDGING, false),

                // Perceiving (P)
                new MbtiQuestion("예정된 계획보다 즉흥적으로 새로운 활동을 하려는 경향이 있다.", PersonalityTrait.PERCEIVING, false),
                new MbtiQuestion("놀이 시간을 마무리해야 할 때, 마지막 순간까지 여러 가지를 하고 싶어한다.", PersonalityTrait.PERCEIVING, false),
                new MbtiQuestion("친구와 놀거나 과제를 할 때 새로운 방식으로 다양한 활동을 시도하려고 한다.", PersonalityTrait.PERCEIVING, false)
        };

        mbtiQuestionRepository.saveAll(Arrays.asList(questions));
    }
}
