package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.AccountDto;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.funds.transfer.utils.TaxesUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureJsonTesters
@SpringBootTest
public class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransferService transferService;

    @Test
    void contextLoads() {
        assertThat(accountService).isNotNull();
        assertThat(transferService).isNotNull();
    }

    @Name("should_return_account")
    @Test
    void should_return_new_account() {
        AccountDto accountDto = new AccountDto(null, "test user", "USD",BigDecimal.valueOf(10000), null);
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
        BigDecimal taxes = getTaxes(BigDecimal.valueOf(1000));
        assertThat(taxes).isEqualTo(BigDecimal.valueOf(2).setScale(2, RoundingMode.FLOOR));

        taxes = getTaxes(BigDecimal.valueOf(5000));
        assertThat(taxes).isEqualTo(BigDecimal.valueOf(25).setScale(2, RoundingMode.FLOOR));
    }

    @Test
    void should_return_total_to_discount_with_taxes() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal totalWithDiscount = getDiscountWithTaxes(amount);
        assertThat(totalWithDiscount).isEqualTo(BigDecimal.valueOf(1002).setScale(2, RoundingMode.FLOOR));

        amount = BigDecimal.valueOf(5000);
        totalWithDiscount = getDiscountWithTaxes(amount);
        assertThat(totalWithDiscount).isEqualTo(BigDecimal.valueOf(5025).setScale(2, RoundingMode.FLOOR));
    }


    @Test
    void should_has_enough_funds() {
        Optional<AccountDto> account = accountService.getAccount(3L);
        boolean hasEnoughFunds = hasEnoughFunds(BigDecimal.valueOf(2000), account.get());
        assertThat(hasEnoughFunds).isTrue();
    }

    @Test
    void should_has_not_enough_funds() {
        Optional<AccountDto> account = accountService.getAccount(3L);
        boolean hasEnoughFunds = hasEnoughFunds(BigDecimal.valueOf(50250), account.get());
        assertThat(hasEnoughFunds).isFalse();
    }

}
