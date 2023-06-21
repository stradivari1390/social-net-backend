package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.AccountResultSearchDto;
import ru.team38.common.dto.AccountSearchDto;
import ru.team38.common.dto.PageDto;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.userservice.data.mappers.AccountMapper;
import ru.team38.userservice.exceptions.status.UnauthorizedException;

import static org.jooq.impl.DSL.min;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final DSLContext dsl;
    private final Account account = Account.ACCOUNT;
    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    public AccountDto getAuthenticatedAccount() throws UnauthorizedException, DataAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails currentUser)) {
            throw new UnauthorizedException("User is not authenticated");
        }
        String email = currentUser.getUsername();

        AccountRecord accountRecord = dsl.selectFrom(account)
                .where(account.EMAIL.eq(email))
                .fetchOne();

        if (accountRecord == null) {
            throw new DataAccessException("No account found with email: " + email);
        }

        return mapper.accountRecordToAccountDto(accountRecord);
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        AccountRecord updateRecord = mapper.accountDtoToAccountRecord(accountDto);
        updateRecord.store();
        return mapper.accountRecordToAccountDto(updateRecord);
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
