package com.kidsworld.kidsping.domain.user.entity;

import com.kidsworld.kidsping.domain.event.entity.EventParticipant;
import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<EventParticipant> eventParticipants = new ArrayList<>();
}
