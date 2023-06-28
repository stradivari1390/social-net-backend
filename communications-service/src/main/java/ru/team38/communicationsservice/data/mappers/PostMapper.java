package ru.team38.communicationsservice.data.mappers;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import ru.team38.common.dto.PostDto;
import ru.team38.common.jooq.tables.records.PostRecord;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper
public interface PostMapper {

    @Mapping(target = "time", expression = "java(toZonedDateTime(postRecord.getTime()))")
    @Mapping(target = "timeChanged", expression = "java(toZonedDateTime(postRecord.getTimeChanged()))")
    @Mapping(target = "publishDate", expression = "java(toZonedDateTime(postRecord.getPublishDate()))")
    PostDto postRecord2RequestPostDto(PostRecord postRecord);

    List<PostDto> postRecordToPostDto(List<PostRecord> records);

    default ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()) : null;
    }

    default ZonedDateTime toZonedDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime().atZone(ZoneId.systemDefault()) : null;
    }
}



