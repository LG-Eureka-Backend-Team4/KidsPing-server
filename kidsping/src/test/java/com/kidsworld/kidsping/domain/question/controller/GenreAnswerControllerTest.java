package com.kidsworld.kidsping.domain.question.controller;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponse;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.jwt.JwtRequestFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = GenreAnswerController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class})
        }
)
class GenreAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreAnswerService genreAnswerService;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @DisplayName("장르 응답 이력 조회에 성공한다")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getGenreAnswerHistory_Success() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        GenreAnswerResponse response = GenreAnswerResponse.builder()
                .kidId(1L)
                .createdAt(now)
                .build();
        List<GenreAnswerResponse> responses = Arrays.asList(response);

        given(genreAnswerService.getGenreAnswerHistory(1L)).willReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/genre-answers/kid/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("아이의 장르 응답 이력을 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data[0].kidId").value(1))
                .andExpect(jsonPath("$.data[0].createdAt").exists());
    }
}