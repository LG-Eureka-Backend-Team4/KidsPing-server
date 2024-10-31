package com.kidsworld.kidsping.domain.user.entity;

import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String userName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isDeleted;



    private String socialId;
    private String refreshToken;
    private String kakaoAccessToken;

    public void updateRefreshToken(String refreshToken) { this.refreshToken = refreshToken;}

    public void removeRefreshToken() { this.refreshToken = null;}

    public void updateKakaoAccessToken(String kakaoAccessToken) { this.kakaoAccessToken = kakaoAccessToken;}

    public void removeKakaoTokens() {
        this.kakaoAccessToken = null;
        this.refreshToken = null;
    }



}