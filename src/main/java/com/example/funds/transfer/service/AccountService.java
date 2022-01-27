package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountBalanceDto;
import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.AccountRequest;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDto> getAll();
    Optional<AccountDto> getAccount(Long accountId);
    AccountDto save(AccountDto accountDto);
    AccountBalanceDto getAccountById(AccountRequest accountDto);
}
