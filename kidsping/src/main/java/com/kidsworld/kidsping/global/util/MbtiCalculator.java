package com.kidsworld.kidsping.global.util;

import com.kidsworld.kidsping.global.common.entity.MbtiScore;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import com.kidsworld.kidsping.global.common.enums.PersonalityTrait;

public class MbtiCalculator {

    public static MbtiStatus determineMbtiType(MbtiScore mbtiScore) {
        String mbti = determinePersonalityTrait(mbtiScore.getEScore(), mbtiScore.getIScore(),
                PersonalityTrait.EXTRAVERSION.getType(), PersonalityTrait.INTROVERSION.getType())
                + determinePersonalityTrait(mbtiScore.getSScore(), mbtiScore.getNScore(),
                PersonalityTrait.SENSING.getType(), PersonalityTrait.INTUITION.getType())
                + determinePersonalityTrait(mbtiScore.getFScore(), mbtiScore.getTScore(),
                PersonalityTrait.FEELING.getType(), PersonalityTrait.THINKING.getType())
                + determinePersonalityTrait(mbtiScore.getPScore(), mbtiScore.getJScore(),
                PersonalityTrait.PERCEIVING.getType(), PersonalityTrait.JUDGING.getType());
        return MbtiStatus.toMbtiStatus(mbti);
    }

    private static String determinePersonalityTrait(int firstScore, int secondScore, String firstType,
                                                    String secondType) {
        return firstScore >= secondScore ? firstType : secondType;
    }
}