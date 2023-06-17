package ru.team38.userservice.data.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.RegisterDto;
import ru.team38.common.dto.StatusCode;
import ru.team38.common.jooq.tables.records.AccountRecord;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto accountRecordToAccountDto(AccountRecord accountRecord);

    @AfterMapping
    default void setStatusCode(@MappingTarget AccountDto accountDto) {
        accountDto.setStatusCode(StatusCode.NONE);
    }

    AccountRecord accountDtoToAccountRecord(AccountDto accountDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", expression = "java(password)")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "isOnline", constant = "false")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "createdOn", expression = "java(java.time.ZonedDateTime.now().toLocalDateTime())")
    @Mapping(target = "messagePermission", constant = "true")
    @Mapping(target = "regDate", expression = "java(java.time.ZonedDateTime.now().toLocalDateTime())")
    @Mapping(target = "birthDate", expression = "java(java.time.ZonedDateTime.now().toLocalDate())")
    AccountRecord registerDto2AccountRecord(RegisterDto registerDto, @Context String password);

    default ZonedDateTime map(LocalDateTime localDateTime) {
        return localDateTime != null ? ZonedDateTime.of(localDateTime, ZoneId.systemDefault()) : null;
    }
}