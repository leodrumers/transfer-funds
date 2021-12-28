package com.example.funds.transfer.utils;

import com.example.funds.transfer.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static com.example.funds.transfer.utils.TaxesUtil.getTaxes;
import static com.example.funds.transfer.utils.TaxesUtil.hasEnoughFunds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TaxesUtilTests {

    @Test
    void contextLoads() {
        TaxesUtil util = new TaxesUtil();
        assertThat(util).isNotNull();
    }

    @Test
    void should_return_tax() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal tax = getTaxes(amount);
        assertThat(tax).isEqualTo(BigDecimal.valueOf(2).setScale(2, RoundingMode.FLOOR));

        amount = BigDecimal.valueOf(5000);
        tax = getTaxes(amount);
        assertThat(tax).isEqualTo(BigDecimal.valueOf(25).setScale(2, RoundingMode.FLOOR));
    }

    @Test
    void should_check_if_has_enough_funds() {
        AccountDto accountDto = new AccountDto(1L, "test user", "USD", BigDecimal.valueOf(4000), LocalDateTime.now());
        BigDecimal amount = BigDecimal.valueOf(5000);
        boolean hasEnough = hasEnoughFunds(amount, accountDto);
        assertThat(hasEnough).isFalse();

        accountDto.setFunds(BigDecimal.valueOf(6000));
        hasEnough = hasEnoughFunds(amount, accountDto);
        assertThat(hasEnough).isTrue();

    }

}
