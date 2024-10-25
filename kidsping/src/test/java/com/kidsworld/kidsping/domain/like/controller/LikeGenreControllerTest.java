package com.kidsworld.kidsping.domain.like.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.like.dto.request.LikeGenreRequest;
import com.kidsworld.kidsping.domain.like.service.LikeGenreService;
import com.kidsworld.kidsping.global.jwt.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = LikeGenreController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class})
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class LikeGenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeGenreService likeGenreService;

    @Autowired
    private ObjectMapper objectMapper;

    private LikeGenreRequest likeRequest;

    @BeforeEach
    public void setup() {
        likeRequest = LikeGenreRequest.builder()
                .kidId(1L)
                .bookId(1L)
                .build();
    }

    @Test
    @DisplayName("도서 좋아요 성공")
    @WithMockUser
    public void likeGenre() throws Exception {
        Mockito.doNothing().when(likeGenreService).like(likeRequest.getKidId(), likeRequest.getBookId());

        mockMvc.perform(post("/api/genres/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("도서 좋아요 취소 성공")
    @WithMockUser
    public void likeCancel() throws Exception {
        Mockito.doNothing().when(likeGenreService).likeCancel(likeRequest.getKidId(), likeRequest.getBookId());

        mockMvc.perform(delete("/api/genres/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("도서 싫어요 성공")
    @WithMockUser
    public void dislikeGenre() throws Exception {
        Mockito.doNothing().when(likeGenreService).dislike(likeRequest.getKidId(), likeRequest.getBookId());

        mockMvc.perform(post("/api/genres/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("도서 싫어요 취소 성공")
    @WithMockUser
    public void dislikeCancel() throws Exception {
        Mockito.doNothing().when(likeGenreService).dislikeCancel(likeRequest.getKidId(), likeRequest.getBookId());

        mockMvc.perform(delete("/api/genres/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRequest)))
                .andExpect(status().isOk());
    }
}
