package com.ebuy.userservice.service;

import lombok.Value;

import javax.persistence.NamedQuery;
import java.math.BigDecimal;

@NamedQuery(
        name = TransactionFund.GET_ALL,
        query = "SELECT new com.ebuy.userservice.service.TransactionFund("
                + "   tx.amount,"
                + "   tx.vendor.vendorCommission,"
                + "   tx.vendor.isFixedCommission"
                + " )"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.type = 'PAYOUT' AND tx.status = 'CONFIRMED'"
)
@NamedQuery(
        name = TransactionFund.GET_BY_VENDOR_ID,
        query = "SELECT new com.ebuy.userservice.service.TransactionFund("
                + "   tx.amount,"
                + "   tx.vendor.vendorCommission,"
                + "   tx.vendor.isFixedCommission"
                + " )"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.type = 'PAYOUT' "
                + " AND tx.status = 'CONFIRMED'"
                + " AND tx.vendor.id = :vendorId"
)
@NamedQuery(
        name = TransactionFund.GET_BY_PERIOD,
        query = "SELECT new com.ebuy.userservice.service.TransactionFund("
                + "   tx.amount,"
                + "   tx.vendor.vendorCommission,"
                + "   tx.vendor.isFixedCommission"
                + " )"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.type = 'PAYOUT'"
                + " AND tx.status = 'CONFIRMED'"
                + " AND tx.date BETWEEN :beginDate AND :endDate"
)
@NamedQuery(
        name = TransactionFund.GET_BY_VENDOR_ID_AND_PERIOD,
        query = "SELECT new com.ebuy.userservice.service.TransactionFund("
                + "   tx.amount,"
                + "   tx.vendor.vendorCommission,"
                + "   tx.vendor.isFixedCommission"
                + " )"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.type = 'PAYOUT'"
                + " AND tx.status = 'CONFIRMED'"
                + " AND tx.vendor.id = :vendorId"
                + " AND tx.date BETWEEN :beginDate AND :endDate"
)
@Value
public class TransactionFund {
    public static final String GET_ALL = "TransactionFund.getAll";
    public static final String GET_BY_VENDOR_ID = "TransactionFund.getByVendorId";
    public static final String GET_BY_PERIOD = "TransactionFund.getByPeriod";
    public static final String GET_BY_VENDOR_ID_AND_PERIOD = "TransactionFund.getByVendorIdAndPeriod";

    BigDecimal amount;
    BigDecimal commission;
    boolean isFixedCommission;
}
