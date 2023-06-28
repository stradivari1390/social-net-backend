package ru.team38.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchDto {
    private Boolean withFriends;
    private Integer page;
    private List <String> sort;
    private Boolean isDeleted;
    private Integer size;
    private Long accountIds;
    private List<String> tags;
    private Long dateFrom;
    private Long dateTo;
    private String author;
}
