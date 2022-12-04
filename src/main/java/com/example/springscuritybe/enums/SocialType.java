package com.example.springscuritybe.enums;


import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum SocialType {
    FACEBOOK("FACEBOOK"),
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO"),
    NONE("");

    private final String ROLE_PREFIX= "ROLE_";
    private String name;


    public String getRoleType(){
        return ROLE_PREFIX + this.name;
    }

    public static SocialType getEnumByName(String name){
        return Arrays.stream(SocialType.values())
                .filter(i -> i.name.equalsIgnoreCase(name))
                .findAny()
                .orElse(SocialType.NONE);
    }




}
