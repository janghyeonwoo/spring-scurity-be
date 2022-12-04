package com.example.springscuritybe.config.security.provider;

import com.example.springscuritybe.config.security.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


@Slf4j
public class CustomProvider implements AuthenticationProvider {
    private final UserDetailsService customUserDetailService;

    public CustomProvider(UserDetailsService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authentication authenticate : {}", authentication);
        UserDetails userDto = customUserDetailService.loadUserByUsername(authentication.getName());
        UsernamePasswordAuthenticationToken  token = new UsernamePasswordAuthenticationToken((UserDto) userDto,null,userDto.getAuthorities());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
