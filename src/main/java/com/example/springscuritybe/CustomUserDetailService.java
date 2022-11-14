package com.example.springscuritybe;

import com.example.springscuritybe.domain.User;
import com.example.springscuritybe.dto.UserDto;
import com.example.springscuritybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("인증에러발생"));
        UserDto userDto = new UserDto(user, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return userDto;
    }
}
