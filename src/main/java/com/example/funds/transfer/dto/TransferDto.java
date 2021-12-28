package com.example.funds.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class TransferDto {

    BigDecimal amount;
    String currency;
    @JsonProperty("origin_account")
    Long originAccount;
    @JsonProperty("destination_account")
    Long destinationAccount;
    String description;
}
