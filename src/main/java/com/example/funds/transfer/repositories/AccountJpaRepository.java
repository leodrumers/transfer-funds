package com.example.funds.transfer.repositories;

import com.example.funds.transfer.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountId(Long accountId);
}
