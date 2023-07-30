package ru.team38.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.post.*;
import ru.team38.gatewayservice.service.PostService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping()
    public ContentPostDto getPost(@RequestParam(value = "withFriends", required = false) Boolean withFriends,
                                  @RequestParam(value = "sort", required = false) List<String> sort,
                                  @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
                                  @RequestParam(value = "accountIds", required = false) UUID accountIds,
                                  @RequestParam(value = "tags", required = false) List<String> tags,
                                  @RequestParam(value = "dateForm", required = false) String dateFrom,
                                  @RequestParam(value = "dateTo", required = false) String dateTo,
                                  @RequestParam(value = "author", required = false) String author,
                                  Pageable pageable) {
        log.info("Executing getPost request");
        return postService.getPost(withFriends, sort, isDeleted, accountIds, tags, dateFrom, dateTo, author, pageable);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable UUID id) {
        log.info("Executing getPostById request");
        return postService.getPostById(id);
    }
    @PostMapping()
    public PostDto getCreatePost(@RequestBody CreatePostDto createPostDto){
        log.info("Executing getCreatePost request");
        return postService.getCreatePost(createPostDto);
    }
    @PutMapping()
    public PostDto getUpdatePost(@RequestBody CreatePostDto createPostDto){
        log.info("Executing getUpdatePost request");
        return postService.getUpdatePost(createPostDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id) {
        log.info("Executing deletePost request");
        return postService.deletePost(id);
    }
}