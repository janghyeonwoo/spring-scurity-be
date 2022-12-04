package com.example.springscuritybe.config.exception.type;

import com.example.springscuritybe.config.exception.BadRequestException;
import com.example.springscuritybe.config.exception.NotFoundErrorException;
import com.example.springscuritybe.config.exception.NotFoundUserException;
import com.example.springscuritybe.config.exception.ReLoginException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    NOT_FOUND_ERROR("일치하는 에러코드가 존재하지 않습니다.", "C001", NotFoundErrorException.class),
    NOT_FOUND_USER("회원정보가 일치하지 않습니다", "C002", NotFoundUserException.class),
    RE_LOGIN("로그인을 진행해주세요" , "C003", ReLoginException.class);

    private String msg;
    private String code;
    private Class<? extends BadRequestException> clazz;

    public static ErrorEnum findByClass(Class<?> clazz){
        return Arrays.stream(ErrorEnum.values())
                .filter(i -> i.clazz.equals(clazz))
                .findAny()
                .orElseThrow(NotFoundErrorException::new);
    }
}
