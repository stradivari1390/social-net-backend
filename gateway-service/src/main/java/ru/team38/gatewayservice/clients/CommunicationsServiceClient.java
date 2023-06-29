package ru.team38.gatewayservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.post.ContentPostDto;

import java.time.ZonedDateTime;
import java.util.List;

@FeignClient(name = "communications-service", url = "${communications-service.url}")
public interface CommunicationsServiceClient {

    @GetMapping("/api/v1/post")
    ResponseEntity<ContentPostDto> getPost(@RequestParam(value = "withFriends", required = false) Boolean withFriends,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "sort", required = false) List<String> sort,
                                           @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
                                           @RequestParam(value = "size", required = false) Integer size,
                                           @RequestParam(value = "accountIds", required = false) Long accountIds,
                                           @RequestParam(value = "tags", required = false) List<String> tags,
                                           @RequestParam(value = "dateForm", required = false) String dateFrom,
                                           @RequestParam(value = "dateTo", required = false) String dateTo,
                                           @RequestParam(value = "author", required = false) String author);
}