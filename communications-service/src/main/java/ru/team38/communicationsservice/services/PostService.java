package ru.team38.communicationsservice.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.team38.common.dto.ContentPostDto;
import ru.team38.common.dto.PostDto;
import ru.team38.common.dto.PostSearchDto;
import ru.team38.communicationsservice.data.repositories.PostRepository;
import ru.team38.communicationsservice.exceptions.NotFoundPostExceptions;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;
    @Autowired
    private HttpServletRequest request;
    public ContentPostDto getPost(PostSearchDto postSearchDto) throws NotFoundPostExceptions {
        try {
            if (postSearchDto.getWithFriends() == null){
                postSearchDto.setWithFriends(false);
            }
            String emailUser = jwtService.getUsernameFromToken(request);
            List<PostDto> listPosts = postRepository.getListPost(postSearchDto, emailUser);
            if (listPosts == null) {
                throw new NotFoundPostExceptions("Post not found.");
            }
            return new ContentPostDto(new ArrayList<>(listPosts));
        } catch (Exception e) {
            log.error("Error occurred while retrieving posts: {}", e.getMessage());
            throw e;
        }
    }
}
