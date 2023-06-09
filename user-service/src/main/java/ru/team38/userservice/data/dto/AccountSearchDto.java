package ru.team38.userservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchDto {
  private Long id;
  private boolean isDeleted;
  private String ids;
  private String blockedByIds;
  private String author;
  private String firstName;
  private String lastName;
  private String city;
  private String country;
  private boolean isBlocked;
  private Integer ageTo;
  private Integer ageFrom;
}
