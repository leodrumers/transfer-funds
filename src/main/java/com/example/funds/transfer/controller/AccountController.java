package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.AccountBalanceDto;
import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.AccountRequest;
import com.example.funds.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAll() {
        return ResponseEntity.ok().body(accountService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> get(@PathVariable Long id) {
        return accountService.getAccount(id).map(
                account -> new ResponseEntity<>(account, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @RequestMapping("/save")
    public ResponseEntity<AccountDto> save(@RequestBody AccountDto account) {
        return ResponseEntity.ok().body(accountService.save(account));
    }

    @PostMapping
    public ResponseEntity<AccountBalanceDto> getAccountById(@RequestBody AccountRequest account) {
        System.out.println(account.getAccount());
        return ResponseEntity.ok(accountService.getAccountById(account));
    }

}
