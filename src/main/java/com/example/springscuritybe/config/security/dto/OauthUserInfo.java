package com.example.springscuritybe.config.security.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class OauthUserInfo {
    Map<String,Object> attributeMap;

    public OauthUserInfo(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }
    private String id;
    private String name;
    private String email;
}
