package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.AccountDto;
import com.example.funds.transfer.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@AutoConfigureJsonTesters
public class AccountControllerTests {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<AccountDto> jsonTransfer;

    @Autowired
    private JacksonTester<AccountDto> jsonTransferResponse;

    @Test
    public void should_save_and_return_object() throws Exception {
        AccountDto newAccountRequest = new AccountDto(null, "test user", "USD", BigDecimal.valueOf(10000), null);
        AccountDto accountResponse = new AccountDto(11L, "test user", "USD", BigDecimal.valueOf(10000), LocalDateTime.now());

        given(accountService.save(newAccountRequest)).willReturn(accountResponse);

        MockHttpServletResponse response = mvc.perform(
                        post("/accounts").contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTransfer.write(newAccountRequest).getJson()))
                .andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonTransferResponse.write(accountResponse).getJson());
    }
}
