package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.entity.Account;
import com.example.funds.transfer.entity.Currency;
import com.example.funds.transfer.mapper.AccountMapper;
import com.example.funds.transfer.repositories.AccountDtoRepository;
import com.example.funds.transfer.repositories.AccountJpaRepository;
import com.example.funds.transfer.repositories.CurrencyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountServiceImpl implements AccountDtoRepository {

    private final AccountJpaRepository repository;
    private final CurrencyJpaRepository currencyRepository;
    private final AccountMapper mapper;

    @Autowired
    public AccountServiceImpl(AccountJpaRepository repository, CurrencyJpaRepository currencyRepository, AccountMapper mapper) {
        this.repository = repository;
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AccountDto> getAll() {
        List<Account> accounts = repository.findAll();
        return mapper.toAccountsDto(accounts);
    }

    @Override
    public Optional<AccountDto> getAccount(Long accountId) {
        return repository.findById(accountId).map(mapper::toAccountDto);
    }

    @Override
    public AccountDto save(AccountDto accountDto) {
        Account account = mapper.toAccount(accountDto);
        Currency currency = currencyRepository.findTopByCurrencyEquals(accountDto.getCurrency());
        account.setCurrency(currency);
        account.setTransfers(new ArrayList<>());
        return mapper.toAccountDto(repository.save(account));
    }
}
