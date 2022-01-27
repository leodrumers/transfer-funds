package com.example.funds.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
public class AccountBalanceDto {
    String status;
    List<String> errors;
    @JsonProperty(value = "account_balance")
    BigDecimal balance;
}
