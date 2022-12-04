package com.example.springscuritybe.config.security.factory;

import com.example.springscuritybe.config.security.dto.GoogleOauthUserInfo;
import com.example.springscuritybe.config.security.dto.OauthUserInfo;
import com.example.springscuritybe.enums.SocialType;

import java.util.Map;

public class OauthUserInfoFactory {
    public static OauthUserInfo getOauthUserInfo(SocialType socialType, Map<String,Object> attributesMap){
        switch (socialType) {
            case GOOGLE: return new GoogleOauthUserInfo(attributesMap);
        }
        return null;
    }
}
