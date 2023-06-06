package ru.team38.userservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("captcha")
@Getter
@Setter
public class CaptchaProperties {
    private int length;
    private char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n',
            'p', 'r', 'w', 'x', 'y', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
}