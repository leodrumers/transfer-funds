package com.example.funds.transfer.utils;

import com.example.funds.transfer.dto.AccountDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxesUtil {

    public TaxesUtil() {
    }

    public static BigDecimal getTaxes(BigDecimal amount) {
        BigDecimal taxes;
        if(amount.compareTo(BigDecimal.valueOf(100)) > 0) {
            taxes = amount.multiply(BigDecimal.valueOf(.02));
        }else {
            taxes = amount.multiply(BigDecimal.valueOf(.05));
        }
        return taxes.setScale(2, RoundingMode.FLOOR);
    }

    public static BigDecimal getDiscountWithTaxes(BigDecimal amount) {
        return amount.add(getTaxes(amount));
    }

    public static boolean hasEnoughFunds(BigDecimal totalWithTaxes, AccountDto account) {
        return totalWithTaxes.compareTo(account.getFunds()) < 1;
    }

}
