package ru.team38.common.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.team38.common.dto.post.PostDto;
import ru.team38.common.dto.post.ReactionDto;
import ru.team38.common.dto.post.TagDto;

import ru.team38.common.jooq.tables.records.PostRecord;
import ru.team38.common.jooq.tables.records.TagRecord;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public interface PostMapper {
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "commentsCount", constant = "0")
    @Mapping(target = "likeAmount", constant = "0")
    @Mapping(target = "myLike", constant = "false")
    PostRecord insertValues2PostRecord(UUID authorId,
                                   String postText,
                                   LocalDateTime time,
                                   String type,
                                   String title,
                                   LocalDateTime publishDate,
                                   String[] tags,
                                   String imagePath,
                                   LocalDateTime timeChanged);

    @Mapping(target = "time", expression = "java(toZonedDateTime(postRecord.getTime()))")
    @Mapping(target = "timeChanged", expression = "java(toZonedDateTime(postRecord.getTimeChanged()))")
    @Mapping(target = "publishDate", expression = "java(toZonedDateTime(postRecord.getPublishDate()))")
    @Mapping(target = "tags", expression = "java(mapText(postRecord.getTags()))")
    PostDto postRecord2PostDto(PostRecord postRecord);
    List<TagDto> tagRecordToTagDto(List<TagRecord> tagRecord);
    default List<TagDto> mapText(String[] text) {
        List<TagDto> tags = new ArrayList<>();
        if (text != null) {
            for (String tag : text) {
                TagDto tagDto = new TagDto();
                tagDto.setName(tag);
                tags.add(tagDto);
            }
        }
        return tags;
    }

    List<PostDto> postRecords2PostDtos(List<PostRecord> records);

    @AfterMapping
    default void setMyReactions(@MappingTarget PostDto postDto) {
        postDto.setMyReactions("Вау");
    }

    @AfterMapping
    default void setReactions(@MappingTarget PostDto postDto) {
        ReactionDto reactionDto = new ReactionDto("Так себе", 1);
        List<ReactionDto> list = new ArrayList<>();
        list.add(reactionDto);
        postDto.setReactions(list);
    }

    default ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()) : null;
    }
}
