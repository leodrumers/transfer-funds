package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.entity.TransferStatus;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureJsonTesters
@SpringBootTest
public class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private TransferServiceImpl transferServiceImpl;

    @Test
    void contextLoads() {
        assertThat(accountService).isNotNull();
        assertThat(accountServiceImpl).isNotNull();
        assertThat(transferServiceImpl).isNotNull();
    }

    @Name("should_return_account")
    @Test
    void should_return_new_account() {
        AccountDto accountDto = new AccountDto();
        accountDto.setName("test user");
        accountDto.setCurrency("USD");
        accountDto.setFunds(BigDecimal.valueOf(10000));
        accountService.save(accountDto);

        List<AccountDto> dtoList = accountService.getAll();
        AccountDto newAccount = dtoList.get(dtoList.size() -1);

        assertThat(newAccount.getName()).isEqualTo("test user");
        assertThat(dtoList.size()).isGreaterThan(0);
    }

    @Test
    void should_return_account() {
        assertThat(accountService.getAccount(1L).isPresent()).isEqualTo(true);
    }

    @Test
    void should_return_taxes() {
        BigDecimal taxes = accountService.getTaxes(BigDecimal.valueOf(1000));
        assertThat(taxes).isEqualTo(BigDecimal.valueOf(2).setScale(2, RoundingMode.FLOOR));

        taxes = accountService.getTaxes(BigDecimal.valueOf(5000));
        assertThat(taxes).isEqualTo(BigDecimal.valueOf(25).setScale(2, RoundingMode.FLOOR));
    }

    @Test
    void should_return_total_to_discount_with_taxes() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal totalWithDiscount = accountService.getDiscountWithTaxes(amount);
        assertThat(totalWithDiscount).isEqualTo(BigDecimal.valueOf(1002).setScale(2, RoundingMode.FLOOR));

        amount = BigDecimal.valueOf(5000);
        totalWithDiscount = accountService.getDiscountWithTaxes(amount);
        assertThat(totalWithDiscount).isEqualTo(BigDecimal.valueOf(5025).setScale(2, RoundingMode.FLOOR));
    }

    @Test
    void should_return_limit_exceeded() {
        boolean limitExceed = accountService.isLimitExceeded(1L);
        assertThat(limitExceed).isTrue();
    }

    @Test
    void should_return_limit_no_exceeded() {
        boolean limitExceed = accountService.isLimitExceeded(2L);
        assertThat(limitExceed).isFalse();
    }

    @Test
    void should_has_enough_funds() {
        Optional<AccountDto> account = accountService.getAccount(3L);
        boolean hasEnoughFunds = accountService.hasEnoughFund(BigDecimal.valueOf(2000), account.get());
        assertThat(hasEnoughFunds).isTrue();
    }

    @Test
    void should_has_not_enough_funds() {
        Optional<AccountDto> account = accountService.getAccount(3L);
        boolean hasEnoughFunds = accountService.hasEnoughFund(BigDecimal.valueOf(50250), account.get());
        assertThat(hasEnoughFunds).isFalse();
    }

    @Test
    void should_get_negative_amount_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(-1));

        TransferStatus status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.NEGATIVE_AMOUNT);
    }

    @Test
    void should_get_same_account_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(1000));
        transfer.setOriginAccount(1L);
        transfer.setDestinationAccount(1L);

        TransferStatus status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.SAME_ACCOUNT);
    }

    @Test
    void should_return_account_not_found_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(1000));
        transfer.setOriginAccount(20L);
        transfer.setDestinationAccount(1L);

        TransferStatus status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.ACCOUNT_NOT_FOUND);

        transfer.setOriginAccount(2L);
        transfer.setDestinationAccount(20L);
        status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.ACCOUNT_NOT_FOUND);
    }

    @Test
    void should_return_insufficient_funds_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(10000));
        transfer.setOriginAccount(2L);
        transfer.setDestinationAccount(1L);

        TransferStatus status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.INSUFFICIENT_FUNDS);
    }

    @Test
    void should_return_limit_exceeded_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(10000));
        transfer.setOriginAccount(1L);
        transfer.setDestinationAccount(2L);

        TransferStatus status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.LIMIT_EXCEEDED);
    }

    @Test
    void should_return_transfer_ok_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(10000));
        transfer.setOriginAccount(3L);
        transfer.setDestinationAccount(2L);

        TransferStatus status = accountService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.TRANSFER_OK);
    }

}
