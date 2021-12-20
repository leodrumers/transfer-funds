package com.example.funds.transfer.repositories;

import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.entity.TransferHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository {
    List<TransferHistory> getAll();
    List<TransferHistory> getByAccount(Long accounId);
    Integer countTransfers(Long accountId);
    TransferHistory save(TransferHistory transferHistory);
    TransferHistory transfer(TransferDto transferDto);
}
