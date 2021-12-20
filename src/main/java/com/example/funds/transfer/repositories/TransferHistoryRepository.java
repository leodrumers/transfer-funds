package com.example.funds.transfer.repositories;

import com.example.funds.transfer.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
}
