package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.entity.Account;
import com.example.funds.transfer.repositories.AccountDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountServiceImpl accountService;

    @Autowired
    public AccountService(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    public List<AccountDto> getAll() {
        return accountService.getAll();
    }

    public Optional<AccountDto> getAccount(Long accountId) {
        return accountService.getAccount(accountId);
    }

    public AccountDto save(AccountDto accountDto) {
        return accountService.save(accountDto);
    }
}
