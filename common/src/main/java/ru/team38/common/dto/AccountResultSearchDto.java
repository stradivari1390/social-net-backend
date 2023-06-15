package ru.team38.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResultSearchDto {
    private PageDto pageDto;
    private CopyOnWriteArraySet<AccountDto> accounts;

    {
        accounts = new CopyOnWriteArraySet<>();
    }
    
    public void setAccount(AccountDto account) {
        accounts.add(account);
    }
}
