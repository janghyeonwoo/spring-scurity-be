package com.example.springscuritybe.config.security.filter;

import com.example.springscuritybe.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomAuthFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto re = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), UserDto.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(re.getUsername(),re.getPassword());
        return super.getAuthenticationManager().authenticate(token);
    }
}
