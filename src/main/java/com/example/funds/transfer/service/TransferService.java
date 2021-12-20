package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.entity.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.funds.transfer.entity.TransferStatus.*;

@Service
public class TransferService {
    private final TransferServiceImpl transferService;
    private final AccountService accountService;

    @Autowired
    public TransferService(TransferServiceImpl service, AccountService accountService) {
        this.transferService = service;
        this.accountService = accountService;
    }

    public List<TransferHistory> getAll() {
        return transferService.getAll();
    }

    public TransferResponse transfer(TransferDto transferDto) {
        TransferResponse response = new TransferResponse();
        List<String> errors = new ArrayList<>();
        response.setTaxCollected(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR));

        Optional<AccountDto> originAccount = accountService.getAccount(transferDto.getOriginAccount());
        Optional<AccountDto> destinationAccount = accountService.getAccount(transferDto.getDestinationAccount());

        TransferStatus transferStatus = accountService.getTransferStatus(transferDto);
        response.setStatus(transferStatus.label);
        if (transferStatus == TRANSFER_OK) {
            BigDecimal taxes = accountService.getTaxes(transferDto.getAmount());
            AccountDto accountDto = originAccount.get();
            AccountDto destinationAccountDto = destinationAccount.get();

            subtractFunds(accountDto, transferDto.getAmount());
            addFunds(destinationAccountDto, transferDto.getAmount());
            saveHistory(transferDto, TRANSFER_SUCCESS.label);

            response.setTaxCollected(taxes.setScale(2, RoundingMode.FLOOR));
            response.setCad(transferDto.getAmount());
            response.setDescription(transferDto.getDescription());
        }

        response.setErrors(errors);
        return response;
    }

    private void saveHistory(TransferDto transferDto, String status) {
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setOriginAccount(transferDto.getOriginAccount());
        transferHistory.setDestinationAccount(transferDto.getDestinationAccount());
        transferHistory.setAmount(transferDto.getAmount());
        transferHistory.setStatus(status);
        transferHistory.setDescription(transferDto.getDescription());
        transferService.save(transferHistory);
    }

    private void subtractFunds(AccountDto accountDto, BigDecimal amount) {
        accountDto.setFunds(accountDto.getFunds().subtract(accountService.getDiscountWithTaxes(amount)).setScale(2, RoundingMode.FLOOR));
        accountService.save(accountDto);
    }

    private void addFunds(AccountDto destinationAccount, BigDecimal amount) {
        destinationAccount.setFunds(destinationAccount.getFunds().add(amount).setScale(2, RoundingMode.FLOOR));
        accountService.save(destinationAccount);
    }

}
