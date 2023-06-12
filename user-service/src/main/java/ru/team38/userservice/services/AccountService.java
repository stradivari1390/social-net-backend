package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.userservice.data.dto.AccountDto;
import ru.team38.userservice.data.dto.AccountSearchDto;
import ru.team38.userservice.data.dto.PageDto;
import ru.team38.userservice.data.dto.RegisterDto;
import ru.team38.userservice.data.mappers.AccountMapper;
import ru.team38.userservice.exceptions.AccountRegister.AccountExistExcepton;
import ru.team38.userservice.exceptions.AccountRegister.AccountRegisterException;
import ru.team38.userservice.exceptions.AccountRegister.PasswordMismatchException;

import static org.jooq.impl.DSL.min;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder encoder;
    private final DSLContext dsl;
    private final Account account = Account.ACCOUNT;
    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    public AccountDto getAccount() {
        Integer minId = dsl.select(min(account.ID)).from(account).fetchOne().into(Integer.class);
        return dsl.select().from(account).where(account.ID.eq(minId)).fetchOptional()
                .orElseThrow(() -> new RuntimeException("no user"))
                .map(record -> mapper.accountRecord2AccountDto((AccountRecord) record));
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        AccountRecord updateRecord = mapper.accountDto2AccountRecord(accountDto);
        return mapper.accountRecord2AccountDto(updateRecord);
    }

    public void deleteAccount() {
        Integer minId = dsl.select(min(account.ID)).from(account).fetchOne().into(Integer.class);
        dsl.delete(account).where(account.ID.eq(minId)).execute();
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
        AccountRecord newAccountRecord = mapper.registerDto2AccountRecord(registerDto, encoder.encode(registerDto.getPassword1()));
        dsl.insertInto(account).set(newAccountRecord).execute();
    }
}
