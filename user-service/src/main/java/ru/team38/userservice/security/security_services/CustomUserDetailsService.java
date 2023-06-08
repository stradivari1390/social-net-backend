package ru.team38.userservice.security.security_services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.team38.userservice.MockUserBase;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MockUserBase mockUserBase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (mockUserBase.getUserBase().containsKey(username)) {
            return new User(username, mockUserBase.getUserBase().get(username), new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}