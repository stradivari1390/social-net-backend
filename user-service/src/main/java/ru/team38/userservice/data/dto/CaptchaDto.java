package ru.team38.userservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CaptchaDto {
    private String secret;
    private String image;
}