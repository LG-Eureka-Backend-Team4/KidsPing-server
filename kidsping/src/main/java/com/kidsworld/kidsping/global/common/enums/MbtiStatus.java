package com.kidsworld.kidsping.global.common.enums;

public enum MbtiStatus {
    ISTJ,
    ISFJ,
    INFJ,
    INTJ,
    ISTP,
    ISFP,
    INFP,
    INTP,
    ESTP,
    ESFP,
    ENFP,
    ENTP,
    ESTJ,
    ESFJ,
    ENFJ,
    ENTJ;

    public static MbtiStatus toMbtiStatus(String mbti) {
        try {
            return MbtiStatus.valueOf(mbti);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid MBTI type: " + mbti);
        }
    }
}