package ru.team38.userservice.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private Boolean isDeleted;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    private StatusCode statusCode;
    private ZonedDateTime regDate;
    private LocalDate birthDate;
    private Boolean messagePermission;
    private ZonedDateTime lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    private Long photoId;
    private String photoName;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;

    public enum StatusCode {
        @JsonProperty("StatusCode.FRIEND")
        FRIEND(0);

        public final int num;

        StatusCode(int num) {
            this.num = num;
        }
    }
}
