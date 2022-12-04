package com.example.springscuritybe.config.security.dto;

import java.util.Map;

public class GoogleOauthUserInfo extends OauthUserInfo{

    public GoogleOauthUserInfo(Map<String, Object> attributeMap) {
        super(attributeMap);
    }

    @Override
    public Map<String, Object> getAttributeMap() {
        return super.getAttributeMap();
    }

    @Override
    public String getId() {
        return (String) super.getAttributeMap().get("sub");
    }

    @Override
    public String getName() {
        return (String) super.getAttributeMap().get("name");
    }

    @Override
    public String getEmail() {
        return (String) super.getAttributeMap().get("email");
    }


}
