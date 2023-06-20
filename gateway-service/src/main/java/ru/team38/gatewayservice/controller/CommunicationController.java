package ru.team38.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.ContentPostDto;
import ru.team38.gatewayservice.service.CommunicationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunicationController {
    private final CommunicationService communicationService;
    @GetMapping("/api/v1/post")
    public ContentPostDto getPost(@RequestParam(value = "withFriends", required = false) Boolean withFriends,
                                  @RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "sort", required = false) List<String> sort,
                                  @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
                                  @RequestParam(value = "size", required = false) Integer size,
                                  @RequestParam(value = "accountIds", required = false) Long accountIds,
                                  @RequestParam(value = "tags", required = false) List<String> tags,
                                  @RequestParam(value = "dateForm", required = false) Long dateFrom,
                                  @RequestParam(value = "dateTo", required = false) Long dateTo,
                                  @RequestParam(value = "author", required = false) String author) {
        log.info("Executing getPost request");
        return communicationService.getPost(withFriends, page, sort, isDeleted, size, accountIds, tags, dateFrom, dateTo, author);
    }
}
