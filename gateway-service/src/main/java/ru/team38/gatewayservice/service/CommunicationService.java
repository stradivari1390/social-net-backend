package ru.team38.gatewayservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.team38.common.dto.ContentPostDto;
import ru.team38.gatewayservice.clients.CommunicationsServiceClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunicationService {
    private final CommunicationsServiceClient communicationsServiceClient;
    public ContentPostDto getPost(Boolean withFriends,
                                  Integer page,
                                  List<String> sort,
                                  Boolean isDeleted,
                                  Integer size,
                                  Long accountIds,
                                  List<String> tags,
                                  Long dateFrom,
                                  Long dateTo,
                                  String author) {
        try {
            ResponseEntity<ContentPostDto> responseEntity = communicationsServiceClient.getPost(
                    withFriends,
                    page,
                    sort,
                    isDeleted,
                    size,
                    accountIds,
                    tags,
                    dateFrom,
                    dateTo,
                    author);
            return responseEntity.getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            throw new RuntimeException(e.contentUTF8(), e);
        }
    }
}
