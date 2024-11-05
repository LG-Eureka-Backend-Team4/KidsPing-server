package com.kidsworld.kidsping.domain.kid.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.kid.dto.request.KidMbtiDiagnosisRequest;
import com.kidsworld.kidsping.domain.kid.dto.response.DeleteKidResponse;
import com.kidsworld.kidsping.domain.kid.dto.response.GetKidMbtiHistoryResponse;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = KidController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class KidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KidService kidService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    @DisplayName("자녀 프로필을 성공적으로 생성한다")
//    void 자녀_프로필_생성_성공() throws Exception {
//        // given
//        CreateKidRequest request = CreateKidRequest.builder()
//                .kidName("김아이")
//                .gender("MALE")
//                .birth("2020-01-01")
//                .build();
//
//        CreateKidResponse response = CreateKidResponse.builder()
//                .kidId(1L)
//                .userId(1L)
//                .kidName("김아이")
//                .gender("MALE")
//                .birth("2020-01-01")
//                .build();
//
//        given(kidService.createKid(any(CreateKidRequest.class)))
//                .willReturn(response);
//
//        String content = objectMapper.writeValueAsString(request);
//
//        // when & then
//        mockMvc.perform(post("/api/kids")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().string("Location", "/api/kids/1"))
//                .andExpect(jsonPath("$.code").value(ExceptionCode.CREATED.getCode()))
//                .andExpect(jsonPath("$.message").value(ExceptionCode.CREATED.getMessage()))
//                .andExpect(jsonPath("$.data.kidId").value(1L))
//                .andExpect(jsonPath("$.data.userId").value(1L))
//                .andExpect(jsonPath("$.data.kidName").value("김아이"))
//                .andExpect(jsonPath("$.data.gender").value("MALE"))
//                .andExpect(jsonPath("$.data.birth").value("2020-01-01"));
//    }

//    @Test
//    @DisplayName("자녀 프로필을 성공적으로 조회한다")
//    void 자녀_프로필_조회_성공() throws Exception {
//        // given
//        Long kidId = 1L;
//        LocalDate currentDate = LocalDate.now();
//        GetKidWithMbtiResponse response = GetKidWithMbtiResponse.builder()
//                .kidId(kidId)
//                .userId(1L)
//                .kidName("김아이")
//                .gender(Gender.MALE)
//                .birth(currentDate)
//                .build();
//
//        given(kidService.getKid(kidId))
//                .willReturn(response);
//
//        // when & then
//        mockMvc.perform(get("/api/kids/{kidId}", kidId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
//                .andExpect(jsonPath("$.message").value("자녀 프로필을 성공적으로 조회했습니다."))
//                .andExpect(jsonPath("$.data.kidId").value(kidId))
//                .andExpect(jsonPath("$.data.userId").value(1L))
//                .andExpect(jsonPath("$.data.kidName").value("김아이"))
//                .andExpect(jsonPath("$.data.gender").value(Gender.MALE))
//                .andExpect(jsonPath("$.data.birth").value(currentDate));
//    }

//    @Test
//    @DisplayName("자녀 프로필을 성공적으로 수정한다")
//    void 자녀_프로필_수정_성공() throws Exception {
//        // given
//        Long kidId = 1L;
//        UpdateKidRequest request = UpdateKidRequest.builder()
//                .kidName("김수정")
//                .gender("FEMALE")
//                .birth("2020-02-02")
//                .build();
//
//        UpdateKidResponse response = UpdateKidResponse.builder()
//                .kidId(kidId)
//                .userId(1L)
//                .kidName("김수정")
//                .gender("FEMALE")
//                .birth("2020-02-02")
//                .build();
//
//        given(kidService.updateKid(eq(kidId), any(UpdateKidRequest.class)))
//                .willReturn(response);
//
//        String content = objectMapper.writeValueAsString(request);
//
//        // when & then
//        mockMvc.perform(put("/api/kids/{kidId}", kidId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
//                .andExpect(jsonPath("$.message").value("자녀 프로필을 성공적으로 수정했습니다."))
//                .andExpect(jsonPath("$.data.kidId").value(kidId))
//                .andExpect(jsonPath("$.data.userId").value(1L))
//                .andExpect(jsonPath("$.data.kidName").value("김수정"))
//                .andExpect(jsonPath("$.data.gender").value("FEMALE"))
//                .andExpect(jsonPath("$.data.birth").value("2020-02-02"));
//    }


    @Test
    @DisplayName("자녀 프로필을 성공적으로 삭제한다")
    void 자녀_프로필_삭제_성공() throws Exception {
        // given
        Long kidId = 1L;
        DeleteKidResponse response = new DeleteKidResponse(kidId);

        given(kidService.deleteKid(kidId))
                .willReturn(response);

        // when & then
        mockMvc.perform(delete("/api/kids/{kidId}", kidId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value("자녀 프로필을 성공적으로 삭제했습니다."))
                .andExpect(jsonPath("$.data.kidId").value(kidId));
    }


    @Test
    @DisplayName("자녀 성향 히스토리를 성공적으로 조회한다")
    void 자녀_성향_히스토리_조회_성공() throws Exception {
        // given
        Long kidId = 1L;
        List<GetKidMbtiHistoryResponse> responses = List.of(
                GetKidMbtiHistoryResponse.builder()
                        .kidMbtiHistoryId(1L)
                        .kidId(kidId)
                        .mbtiStatus("ENFP")
                        .createdAt("2024-01-01T10:00:00")
                        .build(),
                GetKidMbtiHistoryResponse.builder()
                        .kidMbtiHistoryId(2L)
                        .kidId(kidId)
                        .mbtiStatus("INFP")
                        .createdAt("2023-12-01T10:00:00")
                        .build()
        );

        given(kidService.getKidMbtiHistory(kidId))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/kids/{kidId}/mbtihistory", kidId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value("자녀 히스토리를 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].kidMbtiHistoryId").value(1L))
                .andExpect(jsonPath("$.data[0].kidId").value(kidId))
                .andExpect(jsonPath("$.data[0].mbtiStatus").value("ENFP"))
                .andExpect(jsonPath("$.data[0].createdAt").value("2024-01-01T10:00:00"))
                .andExpect(jsonPath("$.data[1].kidMbtiHistoryId").value(2L))
                .andExpect(jsonPath("$.data[1].kidId").value(kidId))
                .andExpect(jsonPath("$.data[1].mbtiStatus").value("INFP"))
                .andExpect(jsonPath("$.data[1].createdAt").value("2023-12-01T10:00:00"));
    }

    @Test
    @DisplayName("자녀 성향을 성공적으로 진단한다")
    void diagnoseKidMbti_successfully() throws Exception {
        // given
        KidMbtiDiagnosisRequest kidMbtiDiagonsisRequest = createKidMbtiDiagonsisRequest(1L, 1, 2, 3, 4, 5,
                6, 7, 8);

        // when & then
        mockMvc.perform(post("/api/kids/mbti/diagonosis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kidMbtiDiagonsisRequest))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static KidMbtiDiagnosisRequest createKidMbtiDiagonsisRequest(Long kidId, int e, int i, int s, int n, int t,
                                                                         int f, int j, int p) {
        return KidMbtiDiagnosisRequest.builder()
                .kidId(kidId)
                .extraversionScore(e)
                .introversionScore(i)
                .sensingScore(s)
                .intuitionScore(n)
                .thinkingScore(t)
                .feelingScore(f)
                .judgingScore(j)
                .perceivingScore(p)
                .build();
    }
}