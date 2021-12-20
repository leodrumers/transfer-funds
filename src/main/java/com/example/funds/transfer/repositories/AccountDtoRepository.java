package com.example.funds.transfer.repositories;

import com.example.funds.transfer.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountDtoRepository {
    List<AccountDto> getAll();
    Optional<AccountDto> getAccount(Long accountId);
    AccountDto save(AccountDto accountDto);
}
