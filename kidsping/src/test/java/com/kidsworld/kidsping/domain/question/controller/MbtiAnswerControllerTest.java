package com.kidsworld.kidsping.domain.question.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kidsworld.kidsping.domain.question.dto.response.MbtiAnswerResponse;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.jwt.JwtRequestFilter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MbtiAnswerController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class})
        }
)
class MbtiAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MbtiAnswerService mbtiAnswerService;

    @DisplayName("자녀의 MBTI 상세 응답을 조회한다.")
    @Test
    @WithMockUser
    void createOrder() throws Exception {
        // given
        MbtiAnswerResponse mbtiAnswerResponse = createMbtiAnswerResponse(1L, 1L, 1, 2, 3, 4, 5, 6, 7, 8);
        given(mbtiAnswerService.getMbtiAnswer(1L)).willReturn(mbtiAnswerResponse);

        // when // then
        mockMvc.perform(get("/api/answers/mbti/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.id").value(mbtiAnswerResponse.getId()))
                .andExpect(jsonPath("$.data.kidId").value(mbtiAnswerResponse.getKidId()))
                .andExpect(jsonPath("$.data.escore").value(mbtiAnswerResponse.getEScore()))
                .andExpect(jsonPath("$.message").value("아이의 성향 응답 이력을 성공적으로 조회했습니다."));
    }

    @DisplayName("자녀의 MBTI 응답 이력 목록을 조회한다.")
    @Test
    @WithMockUser
    void getMbtiAnswers() throws Exception {
        // given
        Long kidId = 1L;
        PageRequest pageable = PageRequest.of(0, 2);
        MbtiAnswerResponse response1 = createMbtiAnswerResponse(1L, 1L, 1, 2, 3, 4, 5, 6, 7, 8);
        MbtiAnswerResponse response2 = createMbtiAnswerResponse(2L, 2L, 11, 12, 13, 14, 15, 16, 17, 18);
        Page<MbtiAnswerResponse> responsePage = new PageImpl<>(List.of(response1, response2), pageable, 2);
        given(mbtiAnswerService.getMbtiAnswers(kidId, pageable)).willReturn(responsePage);

        // when // then
        mockMvc.perform(get("/api/answers/mbti/kid/{kidId}", kidId)
                        .param("page", "0")
                        .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].id").value(response1.getId()))
                .andExpect(jsonPath("$.data.content[1].id").value(response2.getId()))
                .andExpect(jsonPath("$.message").value("아이의 성향 응답 이력 목록을 성공적으로 조회했습니다."));
    }

    @DisplayName("자녀의 MBTI 응답을 삭제한다.")
    @Test
    @WithMockUser(roles = "USER")
    void deleteMbtiAnswer() throws Exception {
        // given
        Long answerId = 1L;
        doNothing().when(mbtiAnswerService).deleteMbtiAnswer(answerId);

        // when // then
        mockMvc.perform(delete("/api/answers/mbti/{answerId}", answerId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static MbtiAnswerResponse createMbtiAnswerResponse(Long kidId, Long id, int i, int e, int s, int n, int f,
                                                               int t, int p, int j) {
        return MbtiAnswerResponse.builder()
                .kidId(kidId)
                .id(id)
                .iScore(i)
                .eScore(e)
                .sScore(s)
                .nScore(n)
                .fScore(f)
                .tScore(t)
                .pScore(p)
                .jScore(j)
                .build();
    }
}