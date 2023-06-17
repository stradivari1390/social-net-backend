package ru.team38.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team38.common.dto.FriendDto;
import ru.team38.userservice.exceptions.FriendsServiceException;
import ru.team38.userservice.services.FriendService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/count")
    public ResponseEntity<List<FriendDto>> getIncomingFriendRequestCount() throws FriendsServiceException {
        return ResponseEntity.ok(friendService.getIncomingFriendRequests());
    }
}