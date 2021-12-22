package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.example.funds.transfer.entity.TransferStatus.TRANSFER_OK;
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
    void should_return_limit_exceeded() {
        boolean limitExceed = transferService.isLimitExceeded(1L);
        assertThat(limitExceed).isTrue();
    }

    @Test
    void should_return_limit_no_exceeded() {
        boolean limitExceed = transferService.isLimitExceeded(2L);
        assertThat(limitExceed).isFalse();
    }

    @Test
    void should_get_negative_amount_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(-1));

        TransferStatus status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.NEGATIVE_AMOUNT);
    }

    @Test
    void should_get_same_account_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(1000));
        transfer.setOriginAccount(1L);
        transfer.setDestinationAccount(1L);

        TransferStatus status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.SAME_ACCOUNT);
    }

    @Test
    void should_return_account_not_found_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(1000));
        transfer.setOriginAccount(20L);
        transfer.setDestinationAccount(1L);

        TransferStatus status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.ACCOUNT_NOT_FOUND);

        transfer.setOriginAccount(2L);
        transfer.setDestinationAccount(20L);
        status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.ACCOUNT_NOT_FOUND);
    }

    @Test
    void should_return_insufficient_funds_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(10000));
        transfer.setOriginAccount(2L);
        transfer.setDestinationAccount(1L);

        TransferStatus status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.INSUFFICIENT_FUNDS);
    }

    @Test
    void should_return_limit_exceeded_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(10000));
        transfer.setOriginAccount(1L);
        transfer.setDestinationAccount(2L);

        TransferStatus status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TransferStatus.LIMIT_EXCEEDED);
    }

    @Test
    void should_return_transfer_ok_status() {
        TransferDto transfer = new TransferDto();
        transfer.setAmount(BigDecimal.valueOf(10000));
        transfer.setOriginAccount(3L);
        transfer.setDestinationAccount(2L);

        TransferStatus status = transferService.getTransferStatus(transfer);
        assertThat(status).isEqualTo(TRANSFER_OK);
    }

    @Test
    void should_return_transfer_response() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(BigDecimal.valueOf(1000));
        transferDto.setOriginAccount(3L);
        transferDto.setDestinationAccount(2L);
        transferDto.setCurrency("USD");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getStatus()).isEqualTo(TRANSFER_OK.label);
        assertThat(transferResponse.getErrors().size()).isEqualTo(0);
    }

    @Test
    void should_return_transfer_response_with_errors() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(BigDecimal.valueOf(1000000));
        transferDto.setOriginAccount(3L);
        transferDto.setDestinationAccount(2L);
        transferDto.setCurrency("USD");

        TransferResponse transferResponse = transferService.transfer(transferDto);

        assertThat(transferResponse.getErrors().size()).isGreaterThan(0);
    }
}
