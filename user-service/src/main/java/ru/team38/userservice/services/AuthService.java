package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.team38.common.dto.LoginForm;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.userservice.exceptions.LogoutFailedException;
import ru.team38.userservice.exceptions.UnauthorizedException;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DSLContext dslContext;
    private AtomicBoolean login = new AtomicBoolean();

    public void getConnection(LoginForm loginForm) throws UsernameNotFoundException, BadCredentialsException {
        AccountRecord account = dslContext.selectFrom(Account.ACCOUNT)
                .where(Account.ACCOUNT.EMAIL.eq(loginForm.getEmail())).fetchOne();

        if(account == null){
            throw new UsernameNotFoundException("Account does not exist");
        }

        boolean isValidPassword = BCrypt.checkpw(loginForm.getPassword(), account.getPassword());

        if(!isValidPassword) {
            throw new BadCredentialsException("Invalid password");
        }
        login.set(isValidPassword);
    }

    public void breakConnection() throws UnauthorizedException, LogoutFailedException {
        if (!getLogin()) {
            throw new UnauthorizedException("User is not logged in");
        }
        login.set(false);
        if (getLogin()) {
            throw new LogoutFailedException("Logout failed");
        }
    }

    public boolean getLogin() {
        return login.get();
    }
}