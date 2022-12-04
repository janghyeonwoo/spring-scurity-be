package com.example.springscuritybe.domain;

import com.example.springscuritybe.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class User extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    @Column
    private String name;

    @Column
    private String id;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updateDate;

//    @Column
//    private String principal;

    @Enumerated(EnumType.STRING)
    @Column
    private SocialType socialType;

}
