package ru.team38.communicationsservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.post.ContentPostDto;
import ru.team38.common.dto.post.CreatePostDto;
import ru.team38.common.dto.post.PostDto;
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
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) throws NotFoundPostExceptions {
        return ResponseEntity.ok(postService.getPostById(id));
    }
    @PostMapping()
    public ResponseEntity<PostDto> getCreatePost(@RequestBody CreatePostDto createPostDto) throws NotFoundPostExceptions {
        return ResponseEntity.ok(postService.getCreatePost(createPostDto));
    }
    @PutMapping()
    public ResponseEntity<PostDto> getUpdatePost(@RequestBody CreatePostDto createPostDto) throws NotFoundPostExceptions {
        return ResponseEntity.ok(postService.getUpdatePost(createPostDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Пост удален");
    }
}
