package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team38.common.dto.*;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.userservice.data.mappers.AccountMapper;
import ru.team38.userservice.exceptions.AccountExistException;
import ru.team38.userservice.exceptions.AccountRegisterException;
import ru.team38.userservice.exceptions.PasswordMismatchException;

import static org.jooq.impl.DSL.min;

@Service
@RequiredArgsConstructor
public class AccountService {
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

    public AccountResultSearchDto findAccount(AccountSearchDto accountSearch, PageDto page) {
        AccountResultSearchDto accountResultSearch = new AccountResultSearchDto();

        Result<Record> records = dsl.select().from(account)
                .where(account.FIRST_NAME.eq(accountSearch.getFirstName()),
                        account.LAST_NAME.eq(accountSearch.getLastName()))
                .limit(page.getSize()).fetch();

        records.forEach(account -> {
            accountResultSearch.setAccount(mapper
                    .accountRecord2AccountDto((AccountRecord) account));
        });

        page.setSize(records.size());
        accountResultSearch.setPageDto(page);

        return accountResultSearch;
    }
}
