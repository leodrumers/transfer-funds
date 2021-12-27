package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.RateResponse;
import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.dto.TransferResponse;
import com.example.funds.transfer.entity.TransferHistory;

import java.util.List;

public interface TransferService  {

    List<TransferHistory> getAll();
    TransferResponse transfer(TransferDto transferDto);
    RateResponse getExchangeRate();
}
