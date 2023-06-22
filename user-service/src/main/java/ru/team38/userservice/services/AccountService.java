package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.AccountResultSearchDto;
import ru.team38.common.dto.AccountSearchDto;
import ru.team38.common.dto.PageDto;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.userservice.data.mappers.AccountMapper;
import ru.team38.userservice.data.repositories.AccountRepository;
import ru.team38.userservice.exceptions.status.BadRequestException;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import static org.jooq.impl.DSL.min;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final DSLContext dsl;
    private final Account account = Account.ACCOUNT;
    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);
    private final AccountRepository accountRepository;

    public AccountDto getAuthenticatedAccount() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.getAccountByEmail(name)
                .orElseThrow(() -> new BadRequestException("User not found"))
                .map(record -> mapper.accountRecordToAccountDto((AccountRecord) record));
    }

    @SneakyThrows
    public AccountDto updateAccount(AccountDto accountDto) {
        AccountDto updateDto = getAuthenticatedAccount();
        for (Field field : AccountDto.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(accountDto) != null) {
                field.set(updateDto, field.get(accountDto));
            }
        }
        return accountRepository.updateAccount(updateDto);
    }

    public void deleteAccount() {
        Long minId = dsl.select(min(account.ID)).from(account).fetchOneInto(Long.class);
        dsl.deleteFrom(account).where(account.ID.eq(minId)).execute();
    }

    public AccountResultSearchDto findAccount(AccountSearchDto accountSearch, PageDto page) {
        AccountResultSearchDto accountResultSearch = new AccountResultSearchDto();

        Result<Record> records = dsl.select().from(account)
                .where(account.FIRST_NAME.eq(accountSearch.getFirstName()),
                        account.LAST_NAME.eq(accountSearch.getLastName()))
                .limit(page.getSize()).fetch();

        records.forEach(acc -> accountResultSearch.setAccount(mapper.accountRecordToAccountDto((AccountRecord) acc)));

        page.setSize(records.size());
        accountResultSearch.setPageDto(page);

        return accountResultSearch;
    }
}
