package com.example.funds.transfer.controller;

import com.example.funds.transfer.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AccountControllerTests {

    @Autowired
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUpd() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void contextLoads(){
        assertThat(accountController).isNotNull();
        assertThat(accountService).isNotNull();
    }

}
