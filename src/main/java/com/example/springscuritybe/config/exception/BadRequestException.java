package com.example.springscuritybe.config.exception;


import com.example.springscuritybe.config.exception.type.ErrorEnum;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private ErrorEnum errorEnum = ErrorEnum.findByClass(this.getClass());

    private String errMsg;
    private String code;

    public BadRequestException() {
        this.errMsg = errorEnum.getMsg();
        this.code = errorEnum.getCode();
    }
}
