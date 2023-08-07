package ru.team38.common.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private long id;
    private UUID authorId;
    private UUID receiverId;
    private NotificationTypeEnum notificationType;
    private ZonedDateTime sendTime;
    private String content;
    private boolean isReaded;
}
