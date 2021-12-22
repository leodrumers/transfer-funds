package com.example.funds.transfer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

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
    void should_transfer_be_error() {

    }
}
