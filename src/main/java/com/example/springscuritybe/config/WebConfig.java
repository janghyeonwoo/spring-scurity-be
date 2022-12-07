package com.example.springscuritybe.config;

import com.example.springscuritybe.config.convert.TestEnumConvert;
import org.aspectj.weaver.ast.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new TestEnumConvert());
    }
}
