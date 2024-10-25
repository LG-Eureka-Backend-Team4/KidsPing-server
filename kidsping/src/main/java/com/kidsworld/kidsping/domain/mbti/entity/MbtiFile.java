package com.kidsworld.kidsping.domain.mbti.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MbtiFile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "random_value")
    private String randomValue;

    @OneToOne
    @JoinColumn(name = "file_id")
    private MbtiInfo mbtiinfo;

}
