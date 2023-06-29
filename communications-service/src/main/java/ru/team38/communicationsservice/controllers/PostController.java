package ru.team38.communicationsservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.post.ContentPostDto;
import ru.team38.common.dto.post.PostSearchDto;
import ru.team38.communicationsservice.exceptions.NotFoundPostExceptions;
import ru.team38.communicationsservice.services.PostService;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping()
    public ResponseEntity<ContentPostDto> getPost(PostSearchDto postSearchDto) throws NotFoundPostExceptions {
        return ResponseEntity.ok(postService.getPost(postSearchDto));
    }
}
