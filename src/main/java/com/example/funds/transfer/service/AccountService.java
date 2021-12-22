package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.entity.TransferStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    List<AccountDto> getAll();
    Optional<AccountDto> getAccount(Long accountId);
    AccountDto save(AccountDto accountDto);
}
