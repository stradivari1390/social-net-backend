package ru.team38.userservice.security.security_services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.team38.gatewayservice.clients.UserServiceClient;
import ru.team38.userservice.security.dto.UserDto;
import ru.team38.userservice.security.oauth.CustomOAuth2User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserServiceClient userServiceClient;

    @Autowired
    public CustomOAuth2UserService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

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
        UserDetails userDetails = userServiceClient.getUserDetailsByEmail(email);
        UserDto userDto = new UserDto();
        if (userDetails == null) {
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setPhone(phone == null ? "" : phone);
            userServiceClient.sendUserDtoDataForRegistration(userDto);
        } else {
            userDto.setName(userDetails.getUsername());
            userDto.setPass(userDetails.getPassword());
            userDto.setEmail("");
            userDto.setPhone("");
        }
        return userDto;
    }
}