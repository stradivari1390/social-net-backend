package ru.team38.userservice.services;

import org.springframework.stereotype.Service;
import ru.team38.userservice.dto.AccountDto;
import ru.team38.userservice.dto.AccountSearchDto;
import ru.team38.userservice.dto.PageDto;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class AccountService {
  public AccountDto getAccount() {
    AccountDto account = AccountDto.builder()
      .id(42352L).isDeleted(false).firstName("Foma").lastName("Kinyaev")
      .email("fkinyaev@something.ru").password("Qwerty!321341").phone("79996662233")
      .phone("https://res.cloudinary.com/duvaewonz/image/upload/v1677442010/xfaazue6lvk7ilrkkycl.jpg")
      .about("Отличное описание аккаунта").city("Москва").country("Россия")
      .statusCode(AccountDto.StatusCode.FRIEND)
      .regDate(ZonedDateTime.parse("2023-05-24T06:25:52.957Z"))
      .birthDate(ZonedDateTime.parse("2023-05-24T06:25:52.957Z"))
      .messagePermission("string")
      .lastOnlineTime(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("Z")))
      .isOnline(true).isBlocked(false).photoId("string").photoName("Аватарка v1")
      .createdOn(ZonedDateTime.parse("2023-05-24T06:25:52.957Z"))
      .updatedOn(ZonedDateTime.parse("2023-05-24T06:25:52.957Z"))
      .build();
    return account;
  }

  public AccountDto updateAccount(AccountDto account) {
    return account;
  }

  public void deleteAccount() {
  }

  public AccountDto findAccount(AccountSearchDto accountSearchDto, PageDto page) {
    return new AccountDto();
  }
}
