package com.kidsworld.kidsping.domain.mbti.service.impl;


import com.kidsworld.kidsping.domain.mbti.dto.response.GetMbtiInfoResponse;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiFile;
import com.kidsworld.kidsping.domain.mbti.entity.MbtiInfo;
import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import com.kidsworld.kidsping.domain.mbti.exception.NotFoundMbtiInfoException;
import com.kidsworld.kidsping.domain.mbti.repository.MbtiInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MbtiInfoServiceTest {

    @InjectMocks
    private MbtiInfoServiceImpl mbtiInfoService;

    @Mock
    private MbtiInfoRepository mbtiInfoRepository;

    @Test
    @DisplayName("MBTI 정보를 성공적으로 조회한다")
    void MBTI_정보_조회_성공() {
        // given
        Long mbtiInfoId = 1L;


        MbtiFile mbtiFile = MbtiFile.builder()
                .id(Long.valueOf("1"))
                .build();


        MbtiInfo mbtiInfo = MbtiInfo.builder()
                .id(mbtiInfoId)
                .mbtiStatus("INFP")
                .title("창의적인 몽상가")
                .roleModel(RoleModel.INFP_YOON_DONGJU)
                .description("상상력이 풍부하고 창의적인 성향")
                .mbtifile(mbtiFile)
                .build();

        given(mbtiInfoRepository.findActiveMbtiInfo(mbtiInfoId))
                .willReturn(Optional.of(mbtiInfo));

        // when
        GetMbtiInfoResponse response = mbtiInfoService.getMbtiInfo(mbtiInfoId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(mbtiInfoId);
        assertThat(response.getMbtiStatus()).isEqualTo("INFP");
        assertThat(response.getTitle()).isEqualTo("창의적인 몽상가");
        assertThat(response.getRoleModel()).isEqualTo(RoleModel.INFP_YOON_DONGJU);
        assertThat(response.getDescription()).isEqualTo("상상력이 풍부하고 창의적인 성향");
        assertThat(response.getFileId()).isEqualTo("1");

        verify(mbtiInfoRepository).findActiveMbtiInfo(mbtiInfoId);
    }

    @Test
    @DisplayName("존재하지 않는 MBTI 정보를 조회하면 예외가 발생한다")
    void MBTI_정보_없음_오류() {
        // given
        given(mbtiInfoRepository.findActiveMbtiInfo(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mbtiInfoService.getMbtiInfo(999L))
                .isInstanceOf(NotFoundMbtiInfoException.class);

        verify(mbtiInfoRepository).findActiveMbtiInfo(999L);
    }
}