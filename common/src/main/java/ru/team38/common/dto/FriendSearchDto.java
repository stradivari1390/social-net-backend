package ru.team38.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendSearchDto {
    private Long id;
    private Boolean isDeleted;
    private Integer idFrom;
    private StatusCode statusCode;
    private Integer idTo;
    private String firstName;
    private LocalDate birthDateFrom;
    private LocalDate birthDateTo;
    private String city;
    private String country;
    private Integer ageFrom;
    private Integer ageTo;
    private StatusCode previousStatusCode;
}
