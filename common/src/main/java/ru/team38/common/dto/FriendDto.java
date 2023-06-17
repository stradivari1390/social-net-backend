package ru.team38.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FriendDto {
    private Long id;
    private Boolean isDeleted;
    private String photo;
    private StatusCode statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private LocalDate birthDate;
    private Boolean isOnline;
    private Long accountFrom;
    private Long accountTo;
    private StatusCode previousStatus;
    private Short rating;
}
