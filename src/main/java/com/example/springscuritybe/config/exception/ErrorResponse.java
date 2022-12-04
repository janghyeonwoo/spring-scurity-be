package com.example.springscuritybe.config.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    private String errMsg;
    private String code;

    private ErrorResponse(String errMsg, String code) {
        this.errMsg = errMsg;
        this.code = code;
    }

    public static ErrorResponse of(String errMsg, String code){
        return new ErrorResponse(errMsg, code);
    }
}
