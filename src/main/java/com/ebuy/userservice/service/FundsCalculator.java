package com.ebuy.userservice.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FundsCalculator implements Calculator {
    @Override
    public BigDecimal calculateRevenue(TransactionFund fund) {
        BigDecimal finalCommission = this.determineCommission(
                fund.getAmount(),
                fund.getCommission(),
                fund.isFixedCommission()
        );
        return this.applyRevenueFormula(fund.getAmount(), finalCommission);
    }

    private BigDecimal applyRevenueFormula(BigDecimal amount, BigDecimal commission) {
        return amount.add(commission);
    }

    @Override
    public BigDecimal calculateVendorRevenue(TransactionVendorFund fund) {
        BigDecimal finalCommission = this.determineCommission(
                fund.getTransferAmount(),
                fund.getCommission(),
                fund.isFixedCommission()
        );
        return this.applyVendorRevenueFormula(fund.getTransferAmount(), fund.getPayoutAmount(), finalCommission);
    }

    private BigDecimal applyVendorRevenueFormula(BigDecimal transferAmount, BigDecimal payoutAmount,
                                                 BigDecimal commission) {
        return transferAmount.subtract(commission.add(payoutAmount));
    }

    private BigDecimal determineCommission(BigDecimal amount, BigDecimal commission, boolean fixed) {
        return fixed
                ? commission
                : this.calculatePercentageCommissionFromAmount(commission, amount);
    }

    private BigDecimal calculatePercentageCommissionFromAmount(BigDecimal commission, BigDecimal amount) {
        int onePercentDenominator = 100;
        return amount.multiply(commission.divide(BigDecimal.valueOf(onePercentDenominator), RoundingMode.HALF_EVEN));
    }

    @Override
    public BigDecimal calculateIncreasedBalance(BigDecimal balance, BigDecimal amount, BigDecimal commission,
                                                boolean fixedCommission) {
        return fixedCommission
                ? this.sumWithFixedCommission(balance, amount, commission)
                : this.sumWithPercentageCommission(balance, amount, commission);
    }

    private BigDecimal sumWithFixedCommission(BigDecimal balance, BigDecimal amount, BigDecimal commission) {
        return balance.add(amount.subtract(commission));
    }

    private BigDecimal sumWithPercentageCommission(BigDecimal balance, BigDecimal amount, BigDecimal commission) {
        return balance.add(amount.subtract(this.calculatePercentageCommissionFromAmount(commission, amount)));
    }

    @Override
    public BigDecimal calculateDecreasedBalance(BigDecimal balance, BigDecimal amount) {
        return balance.subtract(amount);
    }

    public boolean isLessThanOrEqualToZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isGreaterThanZero(BigDecimal amount) {
        return !this.isLessThanOrEqualToZero(amount);
    }
}
