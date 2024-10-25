package com.kidsworld.kidsping.domain.genre.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.genre.dto.request.GenreScoreRequest;
import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;
import com.kidsworld.kidsping.domain.genre.service.GenreScoreService;
import com.kidsworld.kidsping.domain.user.service.UserServiceImpl;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import com.kidsworld.kidsping.global.jwt.JwtRequestFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = GenreScoreController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class})
        }
)
class GenreScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreScoreService genreScoreService;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("장르 점수 추가에 성공한다")
    @WithMockUser
    void addGenreScore_Success() throws Exception {
        // Given
        GenreScoreRequest request = GenreScoreRequest.builder()
                .kidId(1L)
                .genreIds(Arrays.asList(1L, 2L, 3L))
                .build();

        doNothing().when(genreScoreService).addGenreScore(eq(1L), any());

        // When & Then
        mockMvc.perform(post("/api/genre-score/addScore")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("장르 점수 업데이트 성공"));
    }

    @Test
    @DisplayName("존재하지 않는 아이의 장르 점수 추가 시 예외가 발생한다")
    @WithMockUser
    void addGenreScore_KidNotFound() throws Exception {
        // Given
        GenreScoreRequest request = GenreScoreRequest.builder()
                .kidId(999L)
                .genreIds(Arrays.asList(1L, 2L, 3L))
                .build();

        doThrow(new GlobalException(ExceptionCode.NOT_FOUND_KID))
                .when(genreScoreService).addGenreScore(eq(999L), any());

        // When & Then
        mockMvc.perform(post("/api/genre-score/addScore")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionCode.NOT_FOUND_KID.getCode()));
    }

    @Test
    @DisplayName("아이의 최고 점수 장르 조회에 성공한다")
    @WithMockUser
    void getTopGenre_Success() throws Exception {
        // Given
        TopGenreResponse response = TopGenreResponse.builder()
                .genreId(1L)
                .genreTitle("동화")
                .score(100)
                .build();

        given(genreScoreService.getTopGenre(1L)).willReturn(response);

        // When & Then
        mockMvc.perform(get("/api/genre-score/kid/1/top"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.genreId").value(1))
                .andExpect(jsonPath("$.data.genreTitle").value("동화"))
                .andExpect(jsonPath("$.data.score").value(100));
    }
}