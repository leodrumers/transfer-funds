package com.example.funds.transfer.repositories;

import com.example.funds.transfer.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
    List<TransferHistory> getTransferHistoryByOriginAccount(Long accountId);

    Integer countTransferHistoryByOriginAccountAndTransferDateBeforeAndTransferDateAfterAndStatusEquals(
            Long accountId, LocalDateTime before, LocalDateTime after, String status
    );
}
