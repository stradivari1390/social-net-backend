package ru.team38.userservice.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private String name;
    private String username;
    private String pass;
    private String email;
    private String phone;
}