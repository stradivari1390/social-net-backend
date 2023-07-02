package ru.team38.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team38.common.dto.*;
import ru.team38.userservice.exceptions.FriendsServiceException;
import ru.team38.userservice.services.FriendService;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/count")
    public ResponseEntity<Integer> getIncomingFriendRequestsCount() throws FriendsServiceException {
        return ResponseEntity.ok(friendService.getIncomingFriendRequestsCount());
    }

    @GetMapping("")
    public ResponseEntity<PageFriendShortDto> getFriendsByParameters(FriendSearchDto friendSearchDto, PageDto pageDto) {
        return ResponseEntity.ok(friendService.getFriendsByParameters(friendSearchDto, pageDto));
    }
}