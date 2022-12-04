package com.example.springscuritybe.service;

import com.example.springscuritybe.config.exception.ClientException;
import com.example.springscuritybe.config.exception.NotFoundUserException;
import com.example.springscuritybe.config.security.provider.JwtTokenProvider;
import com.example.springscuritybe.domain.User;
import com.example.springscuritybe.dto.JoinDto;
import com.example.springscuritybe.dto.LoginDto;
import com.example.springscuritybe.enums.SocialType;
import com.example.springscuritybe.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;



    public void join(JoinDto joinDto){
        User userEnt = User.builder()
                .id("user1")
                .password("user1")
                .name("pooney")
                .email("user1@gmail.com")
                .socialType(SocialType.NONE)
                .build();
        userRepository.save(userEnt);
    }

    public String login(LoginDto loginDto){
        User user = userRepository.findById(loginDto.getId()).orElseThrow(NotFoundUserException::new);
        return jwtTokenProvider.createToken(user.getId(), Arrays.asList("ADMIN"));
    }


}
