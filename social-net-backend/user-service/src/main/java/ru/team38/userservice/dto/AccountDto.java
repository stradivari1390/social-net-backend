package ru.team38.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
  private Long id;
  private boolean isDeleted;
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
  private ZonedDateTime birthDate;
  private String messagePermission;
  private ZonedDateTime lastOnlineTime;
  private boolean isOnline;
  private boolean isBlocked;
  private String photoId;
  private String photoName;
  private ZonedDateTime createdOn;
  private ZonedDateTime updatedOn;

  public enum StatusCode {
    @JsonProperty("StatusCode.FRIEND")
    FRIEND
  }
}
