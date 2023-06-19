package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.AccountSearchDto;
import ru.team38.common.dto.PageDto;
import ru.team38.userservice.data.mappers.AccountMapper;
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

    public AccountDto findAccount(AccountSearchDto accountSearchDto, PageDto page) {
        return new AccountDto();
    }
}
