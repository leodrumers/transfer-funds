package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.entity.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountServiceImpl accountService;
    private final TransferServiceImpl transferService;

    @Autowired
    public AccountService(AccountServiceImpl accountService, TransferServiceImpl transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
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

    public BigDecimal getTaxes(BigDecimal amount) {
        BigDecimal taxes;
        if(amount.compareTo(BigDecimal.valueOf(1000)) < 1) {
            taxes = amount.multiply(BigDecimal.valueOf(.002));
        }else {
            taxes = amount.multiply(BigDecimal.valueOf(.005));
        }
        return taxes.setScale(2, RoundingMode.FLOOR);
    }

    public BigDecimal getDiscountWithTaxes(BigDecimal amount) {
        return amount.add(getTaxes(amount));
    }

    public boolean isLimitExceeded(Long accountId) {
        List<TransferHistory> transfersNum = transferService.getByAccount(accountId);
        return transfersNum.size() >= 3;
    }

    public boolean hasEnoughFund(BigDecimal totalWithTaxes, AccountDto account) {
        return totalWithTaxes.compareTo(account.getFunds()) < 1;
    }

    public TransferStatus getTransferStatus(TransferDto transferDto) {

        if(transferDto.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            return TransferStatus.NEGATIVE_AMOUNT;
        }

        if(transferDto.getOriginAccount().equals(transferDto.getDestinationAccount())) {
            return TransferStatus.SAME_ACCOUNT;
        }

        Optional<AccountDto> originAccount = accountService.getAccount(transferDto.getOriginAccount());
        Optional<AccountDto> destinationAccount = accountService.getAccount(transferDto.getDestinationAccount());

        if(originAccount.isEmpty() || destinationAccount.isEmpty()) {
            return TransferStatus.ACCOUNT_NOT_FOUND;
        }

        if(this.isLimitExceeded(transferDto.getOriginAccount())){
            return TransferStatus.LIMIT_EXCEEDED;
        }

        if(!this.hasEnoughFund(getDiscountWithTaxes(transferDto.getAmount()), originAccount.get())){
            return TransferStatus.INSUFFICIENT_FUNDS;
        }

        return TransferStatus.TRANSFER_OK;
    }

}
