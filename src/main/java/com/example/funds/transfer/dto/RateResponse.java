package com.example.funds.transfer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class RateResponse {
    private Boolean success;
    private Long timestamp;
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;
}

