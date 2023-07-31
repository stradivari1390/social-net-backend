package ru.team38.communicationsservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team38.common.aspects.LoggingMethod;
import ru.team38.common.dto.notification.NotificationTypeEnum;
import ru.team38.common.dto.post.*;
import ru.team38.common.services.NotificationAddService;
import ru.team38.communicationsservice.data.repositories.PostRepository;
import ru.team38.communicationsservice.services.utils.ConditionUtil;
import ru.team38.communicationsservice.services.utils.DtoAssembler;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final ConditionUtil conditionUtil;
    private final DtoAssembler dtoAssembler;
    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final NotificationAddService notificationService;

    @LoggingMethod
    public ContentPostDto getPost(HttpServletRequest request, PostSearchDto postSearchDto, Pageable pageable) {
        ConditionPostDto conditionPostDto = conditionUtil.createConditionPostDto(postSearchDto);
        String emailUser = jwtService.getUsernameFromToken(request);
        List<PostDto> listPosts = postRepository.getPostDtosByEmail(conditionPostDto, emailUser);
        return dtoAssembler.createContentPostDto(listPosts, pageable);
    }

    @LoggingMethod
    @Transactional
    public PostDto createPost(HttpServletRequest request, CreatePostDto createPostDto) {
        InsertPostDto insertPostDto = dtoAssembler.createInsertPostDto(createPostDto);
        String emailUser = jwtService.getUsernameFromToken(request);
        PostDto post = postRepository.createPost(insertPostDto, emailUser);
        notificationService.addNotification(post.getAuthorId(), post, NotificationTypeEnum.POST);
        return post;
    }

    @LoggingMethod
    @Transactional
    public PostDto updatePost(CreatePostDto createPostDto) {
        try {
            InsertPostDto insertPostDto = dtoAssembler.createInsertPostDto(createPostDto);
            return postRepository.updatePost(insertPostDto, createPostDto.getId());
        } catch (Exception e) {
            log.error("Error occurred while retrieving update post: {}", e.getMessage());
            throw e;
        }
    }

    @LoggingMethod
    public PostDto getPostById(UUID id) {
        return postRepository.getPostDtoById(id);
    }

    @LoggingMethod
    @Transactional
    public void deletePost(UUID id) {
        postRepository.deletePostById(id);
    }

    @LoggingMethod
    public List<TagDto> getTag(String nameTag) {
        return postRepository.getTags(nameTag);
    }
}
