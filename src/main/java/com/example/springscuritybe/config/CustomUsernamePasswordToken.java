package com.example.springscuritybe.config;

import com.example.springscuritybe.config.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomUsernamePasswordToken  extends UsernamePasswordAuthenticationFilter {

    public CustomUsernamePasswordToken(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("ssssssssssssssssssssssssssssssssssssssss");
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto re = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), UserDto.class);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(re.getUsername(),re.getPassword());
        setDetails(request,token);
        return super.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        super.setDetails(request, authRequest);
    }




}
