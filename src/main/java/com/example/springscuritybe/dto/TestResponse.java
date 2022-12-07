package com.example.springscuritybe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestResponse {
    @Schema(description = "이름", defaultValue = "GOOD")
    private String name;

    @Schema(description = "0:사용, 1:미사용", defaultValue = "GOOD")
    private TestEnum test;
}
