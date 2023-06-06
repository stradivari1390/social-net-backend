package ru.team38.userservice.security.security_services;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.team38.userservice.MockUserBase;
import ru.team38.userservice.security.dto.UserDto;
import ru.team38.userservice.security.oauth.CustomOAuth2User;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MockUserBase mockUserBase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        UserDto userDto = processOAuth2User(oAuth2User);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "email", userDto);

        Authentication authentication = new OAuth2AuthenticationToken(customOAuth2User, customOAuth2User.getAuthorities(), userRequest.getClientRegistration().getRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return customOAuth2User;
    }

    public UserDto processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String phone = oAuth2User.getAttribute("phone");
        UserDto userDto = new UserDto();
        if (!mockUserBase.getUserBase().containsKey(email)) {
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setPhone(phone == null ? "" : phone);
            if (email != null && name != null) {
                mockUserBase.getUserBase().put(email, name);
            }
        } else {
            userDto.setName(email);
            userDto.setPass(mockUserBase.getUserBase().get(email));
            userDto.setEmail("");
            userDto.setPhone("");
        }
        return userDto;
    }
}