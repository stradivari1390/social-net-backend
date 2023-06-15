package ru.team38.userservice.data.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.RegisterDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(imports = {LocalDateTime.class, LocalDate.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class AccountMapper {
    private final static AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);

    public abstract AccountRecord accountDto2AccountRecord(AccountDto source);

    public abstract AccountDto accountRecord2AccountDto(AccountRecord source);

    @Mapping(target = "password", expression = "java(password)")
    @Mapping(target = "isOnline", constant = "true")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "regDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "birthDate", expression = "java(LocalDate.ofYearDay(1900, 1))")
    @Mapping(target = "messagePermission", constant = "false")
    @Mapping(target = "statusCode", constant = "0")
    public abstract AccountRecord registerDto2AccountRecord(RegisterDto source, @Context String password);

    protected ZonedDateTime toZonedDateTime(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault());
    }

    protected AccountDto.StatusCode toStatusCode(int value) {
        return AccountDto.StatusCode.values()[0];
    }

    protected int fromStatusCode(AccountDto.StatusCode value) {
        return value.num;
    }
}
