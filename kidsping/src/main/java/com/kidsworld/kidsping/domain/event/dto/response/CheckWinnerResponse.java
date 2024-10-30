package com.kidsworld.kidsping.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CheckWinnerResponse {

    private boolean winningYn;

    public static CheckWinnerResponse of(boolean winningYn) {
        return CheckWinnerResponse.builder()
                .winningYn(winningYn)
                .build();
    }
}
