package com.example.funds.transfer.service;

import com.example.funds.transfer.dto.TransferDto;
import com.example.funds.transfer.entity.TransferStatus;
import com.example.funds.transfer.entity.TransferHistory;
import com.example.funds.transfer.repositories.TransferHistoryRepository;
import com.example.funds.transfer.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransferServiceImpl implements TransferRepository {

    private final TransferHistoryRepository repository;

    @Autowired
    public TransferServiceImpl(TransferHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TransferHistory> getAll() {
        return repository.findAll();
    }

    @Override
    public List<TransferHistory> getByAccount(Long accountId) {
        return repository.getTransferHistoryByOriginAccount(accountId);
    }

    @Override
    public Integer countTransfers(Long accountId) {
        LocalDateTime after = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime before = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return repository.countTransferHistoryByOriginAccountAndTransferDateBeforeAndTransferDateAfterAndStatusEquals(
                accountId, before, after, TransferStatus.TRANSFER_SUCCESS.name()
        );
    }

    @Override
    public TransferHistory save(TransferHistory transferHistory) {
        return repository.save(transferHistory);
    }

    @Override
    public TransferHistory transfer(TransferDto transferDto) {
        return new TransferHistory();
    }
}
