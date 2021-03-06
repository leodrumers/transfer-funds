package com.example.funds.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class AccountDto {
    private Long id;
    private String name;
    private String currency;
    private BigDecimal funds;
    @JsonProperty("creation_date")
    private LocalDateTime creationDate;
}
