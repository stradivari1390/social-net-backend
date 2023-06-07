package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team38.common.jooq.tables.Account;
import ru.team38.userservice.dto.AccountDto;
import ru.team38.userservice.dto.AccountSearchDto;
import ru.team38.userservice.dto.PageDto;
import ru.team38.userservice.dto.RegisterDto;
import ru.team38.userservice.exceptions.AccountRegister.AccountExistExcepton;
import ru.team38.userservice.exceptions.AccountRegister.AccountRegisterException;
import ru.team38.userservice.exceptions.AccountRegister.PasswordMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder encoder;
    private final DSLContext dsl;
    private final Account account = Account.ACCOUNT;


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

    @Transactional
    public void register(RegisterDto registerDto) throws AccountRegisterException {
        if (!registerDto.getPassword1().equals(registerDto.getPassword2())) {
            throw new AccountRegisterException("Passwords mismatch", new PasswordMismatchException());
        }
        Result<Record> result = dsl.select().from(account).where(account.EMAIL.eq(registerDto.getEmail())).fetch();
        if (!result.isEmpty()) {
            throw new AccountRegisterException("User exist", new AccountExistExcepton());
        }
        final String hashPassword = encoder.encode(registerDto.getPassword1());
        dsl.insertInto(account)
                .set(account.EMAIL, registerDto.getEmail())
                .set(account.PASSWORD, hashPassword)
                .set(account.FIRST_NAME, registerDto.getFirstName())
                .set(account.LAST_NAME, registerDto.getLastName())
                .set(account.CREATED_ON, LocalDateTime.now())
                .set(account.REG_DATE, LocalDateTime.now())
                .set(account.UPDATED_ON, LocalDateTime.now())
                .set(account.BIRTH_DATE, LocalDate.ofYearDay(1900, 250))
                .set(account.STATUS_CODE, 0)
                .set(account.IS_ONLINE, true)
                .set(account.IS_BLOCKED, false)
                .set(account.IS_DELETED, false)
                .set(account.MESSAGE_PERMISSION, true)
                .execute();
    }
}
