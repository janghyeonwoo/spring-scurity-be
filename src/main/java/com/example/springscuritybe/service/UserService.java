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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    //    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public void join(JoinDto joinDto) {
        User userEnt = User.builder()
                .id("user1")
                .password("user1")
                .name("pooney")
                .email("user1@gmail.com")
                .socialType(SocialType.NONE)
                .build();
        userRepository.save(userEnt);
    }

    public String login(LoginDto loginDto) {
        User user = userRepository.findById(loginDto.getId()).orElseThrow(NotFoundUserException::new);
        return jwtTokenProvider.createToken(user.getId(), Arrays.asList("ADMIN"));
    }

    public void getCompleteFutuer() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();


        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(getCompletableFuther("A1"), getCompletableFuther("A2"), getCompletableFuther("A3"));
        combinedFuture.get();
        log.info("Thread : {} , Future : {}", Thread.currentThread().getName(), combinedFuture.get());

        CompletableFuture<String> s1 = getCompletableFuther("A4");
        CompletableFuture<String> s2 = getCompletableFuther("A5");
        CompletableFuture<String> s3 = getCompletableFuther("A6");

        log.info("s1 join : {}", s1.get());
        Stream.of(s1,s2,s3).map(CompletableFuture::join)
                .collect(Collectors.toList())
                .forEach(System.out::println);



        long end = System.currentTimeMillis();
        log.info("time : {}", ((end - start) / 1000));
    }

    public CompletableFuture<String> getCompletableFuther(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
                log.info("Async Thread : {}", Thread.currentThread().getName());
                log.info("name : {}", name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "OK1";
        });

    }


}
