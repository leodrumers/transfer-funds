package com.example.funds.transfer.repositories;

import com.example.funds.transfer.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountJpaRepository extends JpaRepository<Account, Long> {
}
