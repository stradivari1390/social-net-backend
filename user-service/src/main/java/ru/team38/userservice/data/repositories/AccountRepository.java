package ru.team38.userservice.data.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final DSLContext dslContext;

    public void save(AccountRecord accountRecord) {
        dslContext.insertInto(Account.ACCOUNT).set(accountRecord).execute();
    }

    public AccountRecord getAccountByEmail(String email) {
        return dslContext.selectFrom(Account.ACCOUNT).where(Account.ACCOUNT.EMAIL.eq(email)).fetchOne();
    }

    public Result<Record> getAllAccountsByEmail(String email) {
        return dslContext.select().from(Account.ACCOUNT).where(Account.ACCOUNT.EMAIL.eq(email)).fetch();
    }
}
