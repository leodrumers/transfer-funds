package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountRepository) {
        this.accountService = accountRepository;
    }

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
    public ResponseEntity<AccountDto> save(@RequestBody AccountDto account) {
        return ResponseEntity.ok().body(accountService.save(account));
    }

}
