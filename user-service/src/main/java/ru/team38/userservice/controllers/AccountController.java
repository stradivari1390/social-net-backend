package ru.team38.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.AccountSearchDto;
import ru.team38.common.dto.PageDto;
import ru.team38.userservice.services.AccountService;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<AccountDto> getAccount() {
        return ResponseEntity.ok(accountService.getAccount());
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody AccountDto account) {
        return ResponseEntity.ok(accountService.updateAccount(account));
    }

    @DeleteMapping("/me")
    public void deleteAccount() {
        accountService.deleteAccount();
    }

    @GetMapping("/search")
    public ResponseEntity<AccountDto> findAccount(AccountSearchDto accountSearch, PageDto page) {
        return ResponseEntity.ok(accountService.findAccount(accountSearch, page));
    }
}
