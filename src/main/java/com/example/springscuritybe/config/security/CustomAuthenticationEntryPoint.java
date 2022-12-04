package com.example.springscuritybe.config.security;

import com.example.springscuritybe.config.exception.ErrorResponse;
import com.example.springscuritybe.config.exception.ReLoginException;
import com.example.springscuritybe.util.JsonConver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ReLoginException e = new ReLoginException();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        System.out.println("sss : " + JsonConver.convertStringToJson(ErrorResponse.of(e.getErrMsg(),e.getCode())));
        response.getWriter().println(JsonConver.convertStringToJson(ErrorResponse.of(e.getErrMsg(),e.getCode())));
        log.error("CustomAuthenticationEntryPoint ERROR : {}", request);

    }
}
