package ru.team38.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.team38.common.dto.notification.NotificationDto;
import ru.team38.common.jooq.tables.records.NotificationRecord;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "sendTime", target = "sentTime")
    NotificationDto notificationRecordToNotificationDto(NotificationRecord notificationRecord);

    default ZonedDateTime map(LocalDateTime localDateTime) {
        return localDateTime != null ? ZonedDateTime.of(localDateTime, ZoneId.systemDefault()) : null;
    }
}
