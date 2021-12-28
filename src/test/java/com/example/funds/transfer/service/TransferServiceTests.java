package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.example.funds.transfer.entity.TransferStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TransferServiceTests {

    @Autowired
    AccountService accountService;

    @Autowired
    TransferService transferService;

    @Autowired
    RestTemplate restTemplate;

    @Test
    void should_return_transfers() {
        assertThat(transferService.getAll().size()).isGreaterThan(0);
    }

    @Test
    void should_return_transfer_response() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(1000), "USD", 3L, 2L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getStatus()).isEqualTo(TRANSFER_OK.label);
        assertThat(transferResponse.getErrors().size()).isEqualTo(0);
    }

    @Test
    void should_return_transfer_response_with_errors() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(1000000), "USD", 3L, 2L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
    }

    @Test
    void should_return_negative_amount() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(-1000), "USD", 3L, 2L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
        assertThat(transferResponse.getErrors().get(0)).isEqualTo(NEGATIVE_AMOUNT.label);
    }

    @Test
    void should_return_same_account() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(10000), "USD", 3L, 3L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
        assertThat(transferResponse.getErrors().get(0)).isEqualTo(SAME_ACCOUNT.label);
    }

    @Test
    void should_return_account_not_found() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(1000), "USD", 19L, 2L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
        assertThat(transferResponse.getErrors().get(0)).isEqualTo(ACCOUNT_NOT_FOUND.label);
    }

    @Test
    void should_return_limit_exceeded() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(1000), "USD", 1L, 2L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
        assertThat(transferResponse.getErrors().get(0)).isEqualTo(LIMIT_EXCEEDED.label);
    }

    @Test
    void should_return_insufficient_funds() {
        TransferDto transferDto = new TransferDto(BigDecimal.valueOf(1000000), "USD", 2L, 3L, "");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
        assertThat(transferResponse.getErrors().get(0)).isEqualTo(INSUFFICIENT_FUNDS.label);
    }
}
