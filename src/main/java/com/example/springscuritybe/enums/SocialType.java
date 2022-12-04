package com.example.springscuritybe.enums;


import lombok.AllArgsConstructor;

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




}
