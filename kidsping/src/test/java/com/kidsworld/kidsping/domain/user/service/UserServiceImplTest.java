package com.kidsworld.kidsping.domain.user.service;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.user.dto.request.RegisterRequest;
import com.kidsworld.kidsping.domain.user.dto.response.GetKidListResponse;
import com.kidsworld.kidsping.domain.user.dto.response.LoginResponse;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import com.kidsworld.kidsping.global.common.entity.UploadedFile;
import com.kidsworld.kidsping.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private JwtUtil jwtUtil;


    @Test
    @DisplayName("유저 ID로 삭제되지 않은 자녀 목록을 조회한다")
    void 자녀목록조회_성공() {
        // Given
        User user = createTestUser(1L, "test@test.com", "password123", "testUser", "01012345678", Role.USER);
        Kid kid1 = createTestKid(1L, "kid1", Gender.MALE, LocalDate.of(2020, 1, 1), user, true);
        Kid kid2 = createTestKid(2L, "kid2", Gender.FEMALE, LocalDate.of(2022, 1, 1), user, false);

        List<Kid> mockKids = Arrays.asList(kid1, kid2);
        when(userRepository.findByUserIdAndIsDeletedFalse(user.getId())).thenReturn(mockKids);

        // When
        List<GetKidListResponse> result = userService.getKidsList(user.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getKidId()).isEqualTo(kid1.getId());
        assertThat(result.get(0).getName()).isEqualTo(kid1.getName());
        assertThat(result.get(0).getProfileUrl()).isEqualTo(kid1.getUploadedFile().getFileUrl());
        assertThat(result.get(1).getKidId()).isEqualTo(kid2.getId());
        assertThat(result.get(1).getName()).isEqualTo(kid2.getName());
        assertThat(result.get(1).getProfileUrl()).isNull();

        verify(userRepository).findByUserIdAndIsDeletedFalse(user.getId());
    }

    @Test
    @DisplayName("회원가입 요청으로 새로운 유저를 저장한다")
    void 새로운유저저장_성공() {
        // Given
        RegisterRequest registerRequest = createTestRegisterRequest(
                "test@test.com", "password123", "testUser", "01012345678", Role.USER
        );
        User expectedUser = createTestUser(1L, "test@test.com", "password123", "testUser", "01012345678", Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // When
        User savedUser = userService.save(registerRequest);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(savedUser.getUserName()).isEqualTo(registerRequest.getUsername());
        assertThat(savedUser.getPhone()).isEqualTo(registerRequest.getPhone());

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("소셜 ID로 유저를 조회한다")
    void 소셜ID유저조회_성공() {
        // Given
        String socialId = "social123";
        User user = createTestUserWithSocialId(1L, "test@test.com", socialId);
        when(userRepository.findBySocialId(socialId)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userService.findBySocialId(socialId);

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getSocialId()).isEqualTo(socialId);

        verify(userRepository).findBySocialId(socialId);
    }

    @Test
    @DisplayName("일반 회원의 리프레시 토큰을 정상적으로 발급한다")
    void 일반회원리프레시토큰발급_성공() {
        // Given
        String oldRefreshToken = "oldRefreshToken";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        User user = createTestUserWithRefreshToken(1L, "test@test.com", "password", oldRefreshToken);
        Kid kid = createTestKid(1L, "kid1", Gender.MALE, LocalDate.of(2020, 1, 1), user, false);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(newAccessToken);
        when(jwtUtil.generateRefreshToken(any(UserDetails.class))).thenReturn(newRefreshToken);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByUserIdAndIsDeletedFalse(user.getId()))
                .thenReturn(Arrays.asList(kid));

        // When
        LoginResponse response = userService.refreshNormalUserToken(user.getEmail());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getJwt()).isEqualTo(newAccessToken);
        assertThat(response.getRefreshToken()).isEqualTo(newRefreshToken);
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getData()).hasSize(1);
        assertThat(response.getData().get(0).getKidId()).isEqualTo(kid.getId());
        assertThat(response.getData().get(0).getName()).isEqualTo(kid.getName());

        verify(userRepository).findByEmail(user.getEmail());
        verify(jwtUtil).generateToken(any(UserDetails.class));
        verify(jwtUtil).generateRefreshToken(any(UserDetails.class));
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getRefreshToken().equals(newRefreshToken)
        ));
        verify(userRepository).findByUserIdAndIsDeletedFalse(user.getId());
    }


    private User createTestUser(Long id, String email, String password, String userName, String phone, Role role) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .userName(userName)
                .phone(phone)
                .role(role)
                .build();
    }

    private User createTestUserWithSocialId(Long id, String email, String socialId) {
        return User.builder()
                .id(id)
                .email(email)
                .socialId(socialId)
                .build();
    }

    private User createTestUserWithRefreshToken(Long id, String email, String password, String refreshToken) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .refreshToken(refreshToken)
                .build();
    }

    private Kid createTestKid(Long id, String name, Gender gender, LocalDate birth, User user, boolean withProfileImage) {
        UploadedFile uploadedFile = null;
        if (withProfileImage) {
            uploadedFile = new UploadedFile("test-file", "server-file");
            uploadedFile.setFileUrl("test-url");
        }

        return Kid.builder()
                .id(id)
                .name(name)
                .gender(gender)
                .birth(birth)
                .user(user)
                .uploadedFile(uploadedFile)
                .isDeleted(false)
                .build();
    }

    private RegisterRequest createTestRegisterRequest(String email, String password, String username, String phone, Role role) {
        return RegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username)
                .phone(phone)
                .role(role)
                .build();
    }


}