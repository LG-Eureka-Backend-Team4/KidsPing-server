package com.kidsworld.kidsping.domain.mbti.controller;

import com.kidsworld.kidsping.domain.mbti.dto.response.GetMbtiInfoResponse;
import com.kidsworld.kidsping.domain.mbti.entity.enums.RoleModel;
import com.kidsworld.kidsping.domain.mbti.exception.NotFoundMbtiInfoException;
import com.kidsworld.kidsping.domain.mbti.service.MbtiInfoService;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MbtiInfoController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class MbtiInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MbtiInfoService mbtiInfoService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("MBTI 정보를 성공적으로 조회한다")
    void MBTI_정보_조회_성공() throws Exception {
        // given
        Long mbtiInfoId = 1L;
        GetMbtiInfoResponse response = GetMbtiInfoResponse.builder()
                .id(mbtiInfoId)
                .mbtiStatus("INFP")
                .title("창의적인 몽상가")
                .roleModel(RoleModel.INFP_YOON_DONGJU)
                .description("상상력이 풍부하고 창의적인 성향")
                .fileId("123")
                .build();

        given(mbtiInfoService.getMbtiInfo(mbtiInfoId))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/mbtiinfo/{mbtiInfoId}", mbtiInfoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value("MBTI 정보를 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data.id").value(mbtiInfoId))
                .andExpect(jsonPath("$.data.mbtiStatus").value("INFP"))
                .andExpect(jsonPath("$.data.title").value("창의적인 몽상가"))
                .andExpect(jsonPath("$.data.roleModel").value("INFP_YOON_DONGJU"))
                .andExpect(jsonPath("$.data.description").value("상상력이 풍부하고 창의적인 성향"))
                .andExpect(jsonPath("$.data.fileId").value("123"));
    }

    @Test
    @DisplayName("존재하지 않는 MBTI 정보를 조회하면 404 응답을 반환한다")
    void MBTI_정보_없음_오류() throws Exception {
        // given
        Long 존재하지않는MbtiId = 999L;
        given(mbtiInfoService.getMbtiInfo(존재하지않는MbtiId))
                .willThrow(new NotFoundMbtiInfoException());

        // when & then
        mockMvc.perform(get("/api/mbtiinfo/{mbtiInfoId}", 존재하지않는MbtiId))
                .andExpect(status().isNotFound());
    }

}