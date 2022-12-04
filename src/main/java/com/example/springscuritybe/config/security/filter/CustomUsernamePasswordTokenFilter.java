package com.example.springscuritybe.config.security.filter;

import com.example.springscuritybe.config.security.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 *  UsernamePasswordAuthenticationFilter의 RequestMatcher는 [Post] login 이다
 */
@Slf4j
public class CustomUsernamePasswordTokenFilter extends UsernamePasswordAuthenticationFilter {

    public CustomUsernamePasswordTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("UsernamePasswordAuthenticationFilter start");
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto re = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), UserDto.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(re.getUsername(),re.getPassword());
        return super.getAuthenticationManager().authenticate(token);
    }

}
