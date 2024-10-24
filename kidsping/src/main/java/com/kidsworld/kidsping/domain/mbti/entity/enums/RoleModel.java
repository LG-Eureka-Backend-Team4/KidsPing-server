package com.kidsworld.kidsping.domain.mbti.entity.enums;

import lombok.Getter;

@Getter
public enum RoleModel {

    // ISTP - 논리적이고 실용적인 장인형
    ISTP_LEESUNSHIN("이순신", "철저한 전략과 실용적인 사고로 나라를 지킨 명장"),

    // ISFJ - 헌신적이고 성실한 수호자형
    ISFJ_SHINSAIMDANG("신사임당", "가족과 자녀 교육에 헌신하며 뛰어난 예술 감각을 발휘한 어머니"),

    // INTJ - 혁신적이고 전략적인 발명가형
    INTJ_SEJONG("세종대왕", "과학과 문화를 발전시킨 혁신적 리더"),

    // INFJ - 통찰력 있고 헌신적인 선지자형
    INFJ_KIMGU("김구", "민족을 위해 헌신한 독립운동의 상징"),

    // ENFP - 창의적이고 열정적인 중재자형
    ENFP_YOO_GWANSUN("유관순", "열정적으로 독립을 위해 싸운 독립운동가"),

    // ENTJ - 지도력 있고 결단력 있는 지휘관형
    ENTJ_KIMHONGDO("김홍도", "조선 후기의 화가로 예술계를 이끈 지도자"),

    // ESTJ - 실용적이고 체계적인 관리자형
    ESTJ_HEUNGSEON("흥선대원군", "조선 말기의 정치와 사회 개혁을 이끈 실용적인 지도자"),

    // ESFJ - 배려심 많고 사교적인 외교관형
    ESFJ_JANGGEUM("장금이", "의료와 요리에 뛰어난 재능을 보인 조선 시대의 의사"),

    // INTP - 논리적이고 호기심 많은 사색가형
    INTP_DASAN("정약용", "실학을 발전시키며 깊이 있는 사고를 한 학자"),

    // INFP - 이상적이고 진취적인 중재자형
    INFP_YOON_DONGJU("윤동주", "자유와 평화를 위해 이상을 노래한 시인"),

    // ENTP - 영리하고 창의적인 논쟁가형
    ENTP_JANG_YOUNGSIL("장영실", "창의적 발명으로 조선을 빛낸 과학자"),

    // ENFJ - 따뜻하고 사교적인 리더형
    ENFJ_YU_GWANSUN("유관순", "독립운동의 상징이 된 민족의 영웅"),

    // ESTP - 모험적이고 적극적인 사업가형
    ESTP_SHINCHAEHO("신채호", "현실적인 문제를 직시하고 행동으로 저항의 메시지를 전달한 독립운동가"),

    // ESFP - 열정적이고 자유로운 연예인형
    ESFP_KIM_GWANGSEOK("김광석", "대중음악의 선두주자로 자유로운 영혼을 표현한 가수"),

    // ISTJ - 현실적이고 책임감 있는 관리자형
    ISTJ_YOOSUNGRYONG("류성룡", "임진왜란 당시 체계적인 전략과 계획으로 국가를 지킨 조선의 관리"),

    // ISFP - 겸손하고 섬세한 예술가형
    ISFP_HANKANG("한강", "감성적이고 예술적인 문학 세계를 펼친 대한민국의 노벨 문학상 수상 작가");

    private final String name;
    private final String description;

    RoleModel(String name, String description) {
        this.name = name;
        this.description = description;
    }
}