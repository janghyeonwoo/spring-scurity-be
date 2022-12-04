package com.example.springscuritybe.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class exceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> clientExceptionHandler(BadRequestException e){
        log.error("..",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(e.getErrMsg(), e.getCode()));
    }
}
