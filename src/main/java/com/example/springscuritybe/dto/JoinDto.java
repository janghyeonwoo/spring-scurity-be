package com.example.springscuritybe.dto;

import com.example.springscuritybe.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDto {
    private String id;
    private String password;
    private String name;
    private String email;
}
