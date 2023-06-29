package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.team38.common.aspects.LoggingMethod;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.FriendDto;
import ru.team38.userservice.data.repositories.FriendRepository;
import ru.team38.userservice.exceptions.DatabaseQueryException;
import ru.team38.userservice.exceptions.FriendsServiceException;
import ru.team38.userservice.exceptions.status.UnauthorizedException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final AccountService accountService;

    public List<FriendDto> getIncomingFriendRequests() throws FriendsServiceException {
        log.info("Executing getIncomingFriendRequests request");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            throw new UnauthorizedException("User is not authenticated");
        }
        AccountDto accountDto = accountService.getAuthenticatedAccount();
        Long userId = accountDto.getId();
        try {
            return friendRepository.getIncomingFriendRequests(userId);
        } catch (DatabaseQueryException e) {
            log.error("Error executing getIncomingFriendRequests request from account ID {}", userId, e);
            throw new FriendsServiceException("Error getting incoming friend requests", e);
        }
    }

    public Integer getIncomingFriendRequestsCount() throws FriendsServiceException {
        log.info("Executing getIncomingFriendRequests request");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            throw new UnauthorizedException("User is not authenticated");
        }
        Long userId = accountService.getAuthenticatedAccount().getId();
        try {
            return friendRepository.countIncomingFriendRequests(userId);
        } catch (DatabaseQueryException e) {
            log.error("Error executing getIncomingFriendRequestsCount request from account ID {}", userId, e);
            throw new FriendsServiceException("Error counting incoming friend requests", e);
        }
    }
}