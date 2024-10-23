package com.kidsworld.kidsping.domain.book.entity.enums;

public enum MbtiType {
    // 16가지 MBTI 유형 정의
    ISTJ("ISTJ"),
    ISFJ("ISFJ"),
    INFJ("INFJ"),
    INTJ("INTJ"),
    ISTP("ISTP"),
    ISFP("ISFP"),
    INFP("INFP"),
    INTP("INTP"),
    ESTP("ESTP"),
    ESFP("ESFP"),
    ENFP("ENFP"),
    ENTP("ENTP"),
    ESTJ("ESTJ"),
    ESFJ("ESFJ"),
    ENFJ("ENFJ"),
    ENTJ("ENTJ");

    private final String mbtiType;

    MbtiType(String mbtiType) {
        this.mbtiType = mbtiType;
    }

    public String getMbtiType() {
        return mbtiType;
    }
}
