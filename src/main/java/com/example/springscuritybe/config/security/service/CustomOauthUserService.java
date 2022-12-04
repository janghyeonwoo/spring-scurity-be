package com.example.springscuritybe.config.security.service;

import com.example.springscuritybe.config.exception.NotFoundUserException;
import com.example.springscuritybe.config.security.dto.OauthUserInfo;
import com.example.springscuritybe.config.security.dto.UserDto;
import com.example.springscuritybe.config.security.factory.OauthUserInfoFactory;
import com.example.springscuritybe.domain.User;
import com.example.springscuritybe.enums.SocialType;
import com.example.springscuritybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;


@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOauthUserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        SocialType socialType = SocialType.getEnumByName(userRequest.getClientRegistration().getRegistrationId());
        OauthUserInfo oauthUserInfo = OauthUserInfoFactory.getOauthUserInfo(socialType, oAuth2User.getAttributes());

        User user = userRepository.findById(oauthUserInfo.getId()).orElseThrow(NotFoundUserException::new);

        log.info("================== start loadUser ==================");
        log.info("ID : {}", user.getId() );
        log.info("NAME : {}", user.getName() );
        log.info("EMAIL : {}", user.getEmail() );
        log.info("================== end loadUser ==================");

        UserDto userDto = new UserDto(user, Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        userDto.setAttributes(oauthUserInfo.getAttributeMap());
        return userDto;
    }
}
