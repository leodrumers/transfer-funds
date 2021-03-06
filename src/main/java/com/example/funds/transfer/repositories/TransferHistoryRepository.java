package com.example.funds.transfer.repositories;

import com.example.funds.transfer.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
    Integer countTransferHistoryByOriginAccountAndTransferDateBeforeAndTransferDateAfterAndStatusEquals(
            Long accountId, LocalDateTime before, LocalDateTime after, String status
    );
}
