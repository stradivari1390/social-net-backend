package ru.team38.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendShortDto {
    private Long id;
    private Boolean isDeleted;
    private StatusCode statusCode;
    private Long friendId;
    private StatusCode previousStatusCode;
    private Short rating;
}
