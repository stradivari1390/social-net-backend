package ru.team38.userservice.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import ru.team38.userservice.repositories.AccountRepository;
import ru.team38.common.jooq.tables.records.AccountRecord;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class AdditionalConfig {

    private final AccountRepository accountRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                AccountRecord account = accountRepository.getAccountByEmail(email);
                if (account == null) {
                    throw new UsernameNotFoundException("Account does not exist");
                }
                return new User(
                        account.getEmail(),
                        account.getPassword(),
                        account.getStatusCode() == 1,
                        true,
                        true,
                        account.getIsBlocked(),
                        new ArrayList<>()
                );
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }
}
