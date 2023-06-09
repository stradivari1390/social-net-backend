package ru.team38.userservice.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.team38.common.jooq.tables.Account;


@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final DSLContext dsl;

    public void save(Account account) {

    }
}
