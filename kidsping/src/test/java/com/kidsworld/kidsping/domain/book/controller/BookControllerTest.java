package com.kidsworld.kidsping.domain.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidsworld.kidsping.domain.book.dto.request.BookRequest;
import com.kidsworld.kidsping.domain.book.dto.response.BookResponse;
import com.kidsworld.kidsping.domain.book.dto.response.GetBookResponse;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import com.kidsworld.kidsping.domain.book.service.BookService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = BookController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class})
        }
)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequest createBookRequest() {
        return BookRequest.builder()
                .genreId(1L)
                .title("테스트 책")
                .summary("테스트 요약")
                .author("테스트 작가")
                .publisher("테스트 출판사")
                .age(5)
                .imageUrl("https://test-image.com")
                .bookMbtiType(MbtiType.ENFP)
                .eScore(70)
                .iScore(30)
                .sScore(40)
                .nScore(60)
                .tScore(45)
                .fScore(55)
                .jScore(35)
                .pScore(65)
                .build();
    }

    private BookResponse createBookResponse() {
        return BookResponse.builder()
                .id(1L)
                .genreId(1L)
                .title("테스트 책")
                .summary("테스트 요약")
                .author("테스트 작가")
                .publisher("테스트 출판사")
                .age(5)
                .imageUrl("https://test-image.com")
                .mbtiType(MbtiType.ENFP)
                .build();
    }

    @Test
    @DisplayName("관리자가 도서를 성공적으로 생성한다")
    @WithMockUser(roles = "ADMIN")
    void createBook_Success() throws Exception {
        // Given
        BookRequest request = createBookRequest();
        BookResponse response = createBookResponse();
        given(bookService.createBook(any(BookRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(ExceptionCode.CREATED.getCode()))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()));
    }

    @Test
    @DisplayName("일반 사용자가 도서 생성을 시도하면 403 에러가 발생한다")
    @WithMockUser(roles = "USER")
    void createBook_Forbidden() throws Exception {
        // Given
        BookRequest request = createBookRequest();
        given(bookService.createBook(any(BookRequest.class)))
                .willThrow(new org.springframework.security.access.AccessDeniedException("Access is denied"));

        // When & Then
        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("도서를 성공적으로 조회한다")
    @WithMockUser
    void getBook_Success() throws Exception {
        // Given
        GetBookResponse response = GetBookResponse.builder()
                .bookInfo(createBookResponse())
                .likeStatus(null)
                .build();

        given(bookService.getBook(1L, null)).willReturn(response);

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.bookInfo.id").value(response.getBookInfo().getId()));
    }

    @Test
    @DisplayName("존재하지 않는 도서를 조회하면 404 에러가 발생한다")
    @WithMockUser
    void getBook_NotFound() throws Exception {
        // Given
        given(bookService.getBook(999L, null))
                .willThrow(new GlobalException(ExceptionCode.NOT_FOUND_BOOK));

        // When & Then
        mockMvc.perform(get("/api/books/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionCode.NOT_FOUND_BOOK.getCode()));
    }

    @Test
    @DisplayName("모든 도서를 성공적으로 조회한다")
    @WithMockUser
    void getAllBooks_Success() throws Exception {
        // Given
        Page<BookResponse> bookPage = new PageImpl<>(
                List.of(createBookResponse()),
                PageRequest.of(0, 10),
                1
        );
        given(bookService.getAllBooks(any())).willReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.content[0].id").value(bookPage.getContent().get(0).getId()));
    }

    @Test
    @DisplayName("관리자가 도서를 성공적으로 수정한다")
    @WithMockUser(roles = "ADMIN")
    void updateBook_Success() throws Exception {
        // Given
        BookRequest request = createBookRequest();
        BookResponse response = createBookResponse();
        given(bookService.updateBook(eq(1L), any(BookRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(put("/api/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.id").value(response.getId()));
    }

    @Test
    @DisplayName("관리자가 도서를 성공적으로 삭제한다")
    @WithMockUser(roles = "ADMIN")
    void deleteBook_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/books/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()));
    }

    @Test
    @DisplayName("장르별 도서를 성공적으로 조회한다")
    @WithMockUser
    void getBooksByGenre_Success() throws Exception {
        // Given
        Page<BookResponse> bookPage = new PageImpl<>(
                List.of(createBookResponse()),
                PageRequest.of(0, 10),
                1
        );
        given(bookService.getBooksByGenre(eq(1L), any())).willReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books/kid/1/genre/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.content[0].id").value(bookPage.getContent().get(0).getId()));
    }

    @Test
    @DisplayName("아이의 MBTI와 궁합이 맞는 MBTI 도서를 성공적으로 조회한다")
    @WithMockUser
    void getCompatibleBooks_Success() throws Exception {
        // Given
        Page<BookResponse> bookPage = new PageImpl<>(List.of(createBookResponse()),
                PageRequest.of(0, 10), 1);
        given(bookService.getCompatibleBooks(eq(1L), any())).willReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books/kid/1/combi")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.content[0].id").value(bookPage.getContent().get(0).getId()))
                .andExpect(jsonPath("$.data.content[0].title").value(bookPage.getContent().get(0).getTitle()));
    }

    @Test
    @DisplayName("존재하지 않는 아이의 MBTI 궁합 도서 조회 시 404 에러 발생한다")
    @WithMockUser
    void getCompatibleBooks_KidNotFound() throws Exception {
        // Given
        given(bookService.getCompatibleBooks(eq(999L), any())).willThrow(new GlobalException(ExceptionCode.NOT_FOUND_KID));

        // When & Then
        mockMvc.perform(get("/api/books/kid/999/combi")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionCode.NOT_FOUND_KID.getCode()));
    }

    @Test
    @DisplayName("아이의 최고 선호 장르의 도서 목록을 성공적으로 조회한다")
    @WithMockUser
    void getTopGenreBooksByKid_Success() throws Exception {
        // Given
        Page<BookResponse> bookPage = new PageImpl<>(
                List.of(createBookResponse()), PageRequest.of(0, 10), 1
        );
        given(bookService.getTopGenreBooksByKid(eq(1L), any())).willReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books/kid/1/genre")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.content[0].id").value(bookPage.getContent().get(0).getId()))
                .andExpect(jsonPath("$.data.content[0].title").value(bookPage.getContent().get(0).getTitle()));
    }

    @Test
    @DisplayName("존재하지 않는 아이의 최고 선호 장르 도서 조회 시 404 에러가 발생한다")
    @WithMockUser
    void getTopGenreBooksByKid_KidNotFound() throws Exception {
        // Given
        given(bookService.getTopGenreBooksByKid(eq(999L), any()))
                .willThrow(new GlobalException(ExceptionCode.NOT_FOUND_KID));

        // When & Then
        mockMvc.perform(get("/api/books/kid/999/genre")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionCode.NOT_FOUND_KID.getCode()));
    }

    @Test
    @DisplayName("아이의 MBTI에 맞는 도서 목록을 성공적으로 조회한다")
    @WithMockUser
    void getRecommendedBooks_Success() throws Exception {
        // Given
        Page<BookResponse> bookPage = new PageImpl<>(
                List.of(createBookResponse()),
                PageRequest.of(0, 10),
                1
        );
        given(bookService.getRecommendedBooks(eq(1L), any())).willReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books/kid/1/mbti")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ExceptionCode.OK.getCode()))
                .andExpect(jsonPath("$.data.content[0].id").value(bookPage.getContent().get(0).getId()))
                .andExpect(jsonPath("$.data.content[0].title").value(bookPage.getContent().get(0).getTitle()));
    }

    @Test
    @DisplayName("MBTI 정보가 없는 아이의 도서 추천 조회 시 404 에러가 발생한다")
    @WithMockUser
    void getRecommendedBooks_MbtiNotFound() throws Exception {
        // Given
        given(bookService.getRecommendedBooks(eq(1L), any()))
                .willThrow(new GlobalException(ExceptionCode.NOT_FOUND_KID_MBTI));

        // When & Then
        mockMvc.perform(get("/api/books/kid/1/mbti")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionCode.NOT_FOUND_KID_MBTI.getCode()));
    }
}