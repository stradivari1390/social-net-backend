package ru.team38.userservice.security.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import ru.team38.userservice.security.dto.UserDto;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class CustomOAuth2User extends DefaultOAuth2User {

    @Getter
    private final UserDto userDto;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey, UserDto userDto) {
        super(authorities, attributes, nameAttributeKey);
        this.userDto = userDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomOAuth2User that)) return false;
        if (!super.equals(o)) return false;
        return getUserDto().equals(that.getUserDto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUserDto());
    }
}