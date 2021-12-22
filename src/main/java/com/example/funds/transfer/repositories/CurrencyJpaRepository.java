package com.example.funds.transfer.repositories;

import com.example.funds.transfer.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyJpaRepository extends JpaRepository<Currency, Long> {
    Currency findByCurrencyEquals(String currency);
    Currency findTopByCurrencyEquals(String currency);
}
