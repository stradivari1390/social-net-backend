package ru.team38.communicationsservice.data.repositories;

import lombok.RequiredArgsConstructor;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;
import ru.team38.common.dto.post.PostDto;
import ru.team38.common.dto.post.PostSearchDto;
import ru.team38.common.jooq.tables.Account;
import ru.team38.common.jooq.tables.Friends;
import ru.team38.common.jooq.tables.Post;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.common.jooq.tables.records.PostRecord;
import ru.team38.common.mappers.PostMapper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final DSLContext dsl;
    private final Post post = Post.POST;
    private final Friends friends = Friends.FRIENDS;
    private final Account account = Account.ACCOUNT;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    public List<PostDto> getListPost (PostSearchDto postSearchDto, String email){
        Long accountId = emailUser2idUser(email);
        if(postSearchDto.getIsDeleted()){
            return null;
        }
        if(postSearchDto.getAccountIds() != null){
            return postMapper.postRecordToPostDto(postsByAccount(postSearchDto));
        }
        if(postSearchDto.getDateTo() != null ){
            return postMapper.postRecordToPostDto(postsSearch(postSearchDto, accountId));
        }
        return postMapper.postRecordToPostDto(allPosts(postSearchDto, accountId));
    }
    private SortField<?> sort (PostSearchDto postSearchDto){
        List<String> querySort = new ArrayList<>(postSearchDto.getSort());
        SortField<?> sortField = null;
        String type = querySort.get(0);
        String sort = querySort.get(1);
        String DESC = "desc";
        String TYPE = "time";
        if(type.equals(TYPE)){
            Field<?> field = post.TIME;
            sortField = sort.equalsIgnoreCase(DESC) ? field.desc() : field.asc();
        }
        return sortField;
    }
    private List<PostRecord> allPosts(PostSearchDto postSearchDto, Long accountId) {
        int pageSize = postSearchDto.getSize();
        if (postSearchDto.getWithFriends()) {
            return dsl.select()
                    .from(post)
                    .join(friends)
                    .on(friends.REQUESTED_ACCOUNT_ID.eq(post.AUTHOR_ID))
                    .where(friends.ACCOUNT_FROM_ID.eq(accountId))
                    .and(friends.STATUS_CODE.eq("FRIEND"))
                    .orderBy(sort(postSearchDto))
                    .limit(pageSize)
                    .fetch()
                    .into(PostRecord.class);
        }else if(postSearchDto.getTags() != null){
            return dsl.select()
                    .from(post)
                    //.where(tagsCondition(postSearchDto.getTags()))
                    .orderBy(sort(postSearchDto))
                    .limit(pageSize)
                    .fetch()
                    .into(PostRecord.class);
        }else {
            Integer count = postSearchDto.getPage();
            int offset = (count != null) ? count * pageSize : 0;
            return dsl.select()
                    .from(post)
                    .orderBy(sort(postSearchDto))
                    .limit(pageSize)
                    .offset(offset)
                    .fetch()
                    .into(PostRecord.class);
        }
    }
    private List<PostRecord> postsByAccount(PostSearchDto postSearchDto) {
        return dsl.select()
                .from(post)
                .where(post.AUTHOR_ID.eq(postSearchDto.getAccountIds()))
                .orderBy(sort(postSearchDto))
                .limit(postSearchDto.getSize())
                .fetch()
                .into(PostRecord.class);
    }
    private List<PostRecord> postsSearch(PostSearchDto postSearchDto, Long accountId) {
        return dsl.select()
                .from(post)
                .join(friends)
                .on(friends.REQUESTED_ACCOUNT_ID.eq(post.AUTHOR_ID))
                .where(friends.ACCOUNT_FROM_ID.eq(accountId))
                .and(friends.STATUS_CODE.eq("FRIEND"))
                .and(authorIdCondition(postSearchDto.getAuthor()))
                //.and(tagsCondition(postSearchDto.getTags()))
                .and(timeCondition(postSearchDto.getDateTo(), postSearchDto.getDateFrom()))
                .orderBy(sort(postSearchDto))
                .limit(postSearchDto.getSize())
                .fetch()
                .into(PostRecord.class);
    }
    /*private Condition tagsCondition(List<String> tags) {
        Condition tagsCondition = DSL.trueCondition();
        for (String tag : tags) {
            tagsCondition = tagsCondition.and(DSL.coalesce(post.TAGS.cast(String[].class), DSL.field("ARRAY['']")).like("%" + tag + "%"));
        }
        return tagsCondition;
    }

     */

    private Condition authorIdCondition(String author) {
        Condition authorIdCondition = DSL.trueCondition();
        Field<Long> authorIdField = null;
        if (author != null) {
            String[] name = author.split(" ");
            if (name.length == 1) {
                authorIdField = DSL.field(DSL.select(account.ID)
                        .from(account)
                        .where(DSL.lower(account.FIRST_NAME).eq(name[0].toLowerCase()))
                        .or(DSL.lower(account.LAST_NAME).eq(name[0].toLowerCase()))
                        .limit(1));
            } else if (name.length >= 2) {
                authorIdField = DSL.field(DSL.select(account.ID)
                        .from(account)
                        .where(DSL.lower(account.FIRST_NAME).eq(name[0].toLowerCase()).and(DSL.lower(account.LAST_NAME).eq(name[1].toLowerCase())))
                        .or(DSL.lower(account.FIRST_NAME).eq(name[1].toLowerCase()).and(DSL.lower(account.LAST_NAME).eq(name[0].toLowerCase())))
                        .limit(1));
            }
        }
        if (authorIdField != null) {
            authorIdCondition = post.AUTHOR_ID.in(authorIdField);
        }
        return authorIdCondition;
    }

    private Condition timeCondition(String to, String from) {
        Condition timeCondition = DSL.trueCondition();
        if (from != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            LocalDateTime fromDateTime = LocalDateTime.parse(from, formatter);
            LocalDateTime toDateTime = LocalDateTime.parse(to, formatter);

            timeCondition = post.PUBLISH_DATE.between(fromDateTime, toDateTime);
        }
        return timeCondition;
    }
    private Long emailUser2idUser(String email){
        AccountRecord accountRecord = dsl.selectFrom(account)
                .where(account.EMAIL.eq(email))
                .fetchOne();
        assert accountRecord != null;
        return accountRecord.getId();
    }
}
