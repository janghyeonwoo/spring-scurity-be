package com.example.springscuritybe.config.convert;

import com.example.springscuritybe.dto.TestEnum;
import org.springframework.core.convert.converter.Converter;

public class TestEnumConvert implements Converter<String, TestEnum> {

    @Override
    public TestEnum convert(String source) {
        return TestEnum.ofType(source);
    }
}
