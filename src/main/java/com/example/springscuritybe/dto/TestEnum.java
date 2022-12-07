package com.example.springscuritybe.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TestEnum {
    YES("Yes", 1),
    NO("No",0);

    private String text;
    private Integer type;

    @JsonCreator
    public static TestEnum ofType(String value){
        return Stream.of(TestEnum.values())
                .filter(i -> i.text.equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    public String getValue() {
        return this.text;
    }
}
