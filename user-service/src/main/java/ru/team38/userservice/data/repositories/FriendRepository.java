package ru.team38.userservice.data.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;
import ru.team38.common.dto.FriendDto;
import ru.team38.common.dto.StatusCode;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.Friends;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.common.jooq.tables.records.FriendsRecord;
import ru.team38.common.mappers.FriendDtoMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    private final DSLContext dsl;
    private final FriendDtoMapper friendDtoMapper = Mappers.getMapper(FriendDtoMapper.class);

    public int countIncomingFriendRequests(Long accountId) {
        return dsl.fetchCount(Friends.FRIENDS, Friends.FRIENDS.ACCOUNT_FROM_ID.eq(accountId)
                .and(Friends.FRIENDS.STATUS_CODE.eq(StatusCode.REQUEST_FROM.name())));
    }

    public List<FriendDto> getIncomingFriendRequests(Long accountId) {
        return dsl.select()
                .from(Friends.FRIENDS)
                .join(Account.ACCOUNT)
                .on(Friends.FRIENDS.REQUESTED_ACCOUNT_ID.eq(Account.ACCOUNT.ID))
                .where(Friends.FRIENDS.ACCOUNT_FROM_ID.eq(accountId))
                .and(Friends.FRIENDS.STATUS_CODE.eq(StatusCode.REQUEST_FROM.name()))
                .fetch()
                .map(this::mapToFriendDto);
    }

    private FriendDto mapToFriendDto(Record rec) {
        FriendsRecord friendsRecord = rec.into(Friends.FRIENDS);
        AccountRecord accountRecord = rec.into(Account.ACCOUNT);
        return friendDtoMapper.mapToFriendDto(friendsRecord, accountRecord);
    }
}