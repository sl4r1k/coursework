package com.ebuy.userservice.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface Calculator {
    BigDecimal calculateRevenue(TransactionFund fund);

    BigDecimal calculateVendorRevenue(TransactionVendorFund fund);

    BigDecimal calculateIncreasedBalance(BigDecimal balance, BigDecimal amount,
                                         BigDecimal commission, boolean fixedCommission);

    BigDecimal calculateDecreasedBalance(BigDecimal balance, BigDecimal amount);
}
