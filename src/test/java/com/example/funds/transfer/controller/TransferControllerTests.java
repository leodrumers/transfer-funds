package com.example.funds.transfer.controller;

import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferStatus;
import com.example.funds.transfer.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.funds.transfer.entity.TransferStatus.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransferController.class)
@AutoConfigureJsonTesters
public class TransferControllerTests {

    @MockBean
    private TransferService transferService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<TransferDto> jsonTransfer;

    @Autowired
    private JacksonTester<TransferResponse> jsonTransferResponse;

    @Test
    void post_transfer_ok() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(1000);
        TransferDto transferRequest = new TransferDto(amount, "USD", 3L, 2L, "");

        List<String> errors = new ArrayList<>();
        TransferResponse transferResponse = new TransferResponse(TRANSFER_OK.label, errors, BigDecimal.valueOf(46.11) , BigDecimal.valueOf(5200), "" );

        given(transferService.transfer(transferRequest)).willReturn(transferResponse);

        MockHttpServletResponse response = mvc.perform(
                post("/transfers/transfer_funds").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransfer.write(transferRequest).getJson()))
                .andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonTransferResponse.write(transferResponse).getJson());

    }

    @Test
    void post_transfer_error() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(-1000);
        TransferDto transferRequest = new TransferDto(amount, "USD", 3L, 2L, "");

        List<String> errors = List.of(new String[]{NEGATIVE_AMOUNT.label});

        TransferResponse transferResponse = new TransferResponse(TRANSFER_ERROR.label, errors, BigDecimal.valueOf(0.0) , null, null );

        given(transferService.transfer(transferRequest)).willReturn(transferResponse);

        MockHttpServletResponse response = mvc.perform(
                post("/transfers/transfer_funds").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransfer.write(transferRequest).getJson()))
                .andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(response.getContentAsString()).isEqualTo(jsonTransferResponse.write(transferResponse).getJson());

    }
}
