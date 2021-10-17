package com.ebuy.userservice.service;

import lombok.Value;

import javax.persistence.NamedQuery;
import java.math.BigDecimal;

@NamedQuery(
        name = TransactionVendorFund.GET_ALL,
        query = "SELECT new com.ebuy.userservice.service.TransactionVendorFund("
                + "     tTx.amount,"
                + "     pTx.amount,"
                + "     tTx.vendor.vendorCommission,"
                + "     tTx.vendor.isFixedCommission,"
                + "     count(tTx)"
                + ")"
                + " FROM VendorTransaction AS tTx"
                + " JOIN VendorTransaction AS pTx"
                + "     ON tTx.vendor = pTx.vendor"
                + "     AND tTx.status = pTx.status"
                + " WHERE tTx.type = 'TRANSFER'"
                + " AND pTx.type = 'PAYOUT'"
                + " AND tTx.status = 'CONFIRMED'"
                + " AND tTx.vendor.id = :vendorId"
)
@NamedQuery(
        name = TransactionVendorFund.GET_BY_PERIOD,
        query = "SELECT new com.ebuy.userservice.service.TransactionVendorFund("
                + "     tTx.amount,"
                + "     pTx.amount,"
                + "     tTx.vendor.vendorCommission,"
                + "     tTx.vendor.isFixedCommission,"
                + "     count(tTx)"
                + ")"
                + " FROM VendorTransaction AS tTx"
                + " JOIN VendorTransaction AS pTx"
                + "     ON tTx.vendor = pTx.vendor"
                + "     AND tTx.status = pTx.status"
                + " WHERE tTx.type = 'TRANSFER'"
                + " AND pTx.type = 'PAYOUT'"
                + " AND tTx.status = 'CONFIRMED'"
                + " AND tTx.vendor.id = :vendorId"
                + " AND tTx.date BETWEEN :beginDate AND :endDate"
                + " AND pTx.date BETWEEN :beginDate AND :endDate"
)
@Value
public class TransactionVendorFund {
    public static final String GET_ALL = "TransactionVendorFund.getAll";
    public static final String GET_BY_PERIOD = "TransactionVendorFund.getByPeriod";

    BigDecimal transferAmount;
    BigDecimal payoutAmount;
    BigDecimal commission;
    boolean isFixedCommission;
    long count;
}
