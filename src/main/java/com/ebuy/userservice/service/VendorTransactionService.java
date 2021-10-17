package com.ebuy.userservice.service;

import com.ebuy.userservice.embedded.TransactionStatus;
import com.ebuy.userservice.embedded.TransactionType;
import com.ebuy.userservice.entity.VendorTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class VendorTransactionService extends Dao<VendorTransaction> {
    private final VendorService vendorService;
    private final VendorTransactionEmailService emailService;
    private final Calculator calculator;

    @Autowired
    public VendorTransactionService(
            VendorService vendorService,
            VendorTransactionEmailService emailService,
            Calculator calculator
    ) {
        super(VendorTransaction.class);
        this.vendorService = vendorService;
        this.emailService = emailService;
        this.calculator = calculator;
    }

    @Override
    public void save(VendorTransaction transaction) {
        this.updateVendorBalanceIfConfirmed(transaction.getStatus(), transaction);
        super.save(transaction);
    }

    private void updateVendorBalanceIfConfirmed(TransactionStatus status, VendorTransaction transaction) {
        if (status.equals(TransactionStatus.CONFIRMED)) {
            this.vendorService.updateBalanceByIdAndTransactionType(
                    transaction.getVendor().getId(),
                    transaction.getType(),
                    transaction.getAmount()
            );
        }
    }

    public void updateStatusById(Long id, TransactionStatus newStatus) {
        VendorTransaction transaction = this.getById(id);
        this.updateVendorBalanceIfConfirmed(newStatus, transaction);
        this.emailService.sendNotificationByTransactionType(
                transaction.getType(),
                transaction.getVendor().getEmailAddress()
        );
        this.manager.createNamedQuery(
                VendorTransaction.UPDATE_STATUS_BY_ID
        ).setParameter("newStatus", newStatus)
                .setParameter("id", id)
                .executeUpdate();
    }

    public BigDecimal getTotalRevenue() {
        return this.toRevenue(
                this.manager.createNamedQuery(
                        TransactionFund.GET_ALL,
                        TransactionFund.class
                ).getResultList()
        );
    }

    public BigDecimal getRevenueByVendorId(Long vendorId) {
        return this.toRevenue(
                this.manager.createNamedQuery(
                        TransactionFund.GET_BY_VENDOR_ID,
                        TransactionFund.class
                ).setParameter("vendorId", vendorId)
                        .getResultList()
        );
    }

    public BigDecimal getRevenueByPeriod(Date beginDate, Date endDate) {
        return this.toRevenue(
                this.manager.createNamedQuery(
                        TransactionFund.GET_BY_PERIOD,
                        TransactionFund.class
                ).setParameter("beginDate", beginDate)
                        .setParameter("endDate", endDate)
                        .getResultList()
        );
    }

    public BigDecimal getRevenueByVendorIdAndPeriod(Long vendorId, Date beginDate, Date endDate) {
        return this.toRevenue(
                this.manager.createNamedQuery(
                        TransactionFund.GET_BY_VENDOR_ID_AND_PERIOD,
                        TransactionFund.class
                ).setParameter("vendorId", vendorId)
                        .setParameter("beginDate", beginDate)
                        .setParameter("endDate", endDate)
                        .getResultList()
        );
    }

    private BigDecimal toRevenue(List<TransactionFund> funds) {
        return funds.stream()
                .map(this.calculator::calculateRevenue)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalStateException::new);
    }

    public BigDecimal getVendorTotalRevenueByVendorId(Long vendorId) {
        return this.toVendorRevenue(
                this.manager.createNamedQuery(
                        TransactionVendorFund.GET_ALL,
                        TransactionVendorFund.class
                ).setParameter("vendorId", vendorId)
                        .getResultList()
        );
    }

    public BigDecimal getVendorRevenueByPeriodByVendorId(Long vendorId, Date beginDate, Date endDate) {
        return this.toVendorRevenue(
                this.manager.createNamedQuery(
                        TransactionVendorFund.GET_BY_PERIOD,
                        TransactionVendorFund.class
                ).setParameter("vendorId", vendorId)
                        .setParameter("beginDate", beginDate)
                        .setParameter("endDate", endDate)
                        .getResultList()
        );
    }

    private BigDecimal toVendorRevenue(List<TransactionVendorFund> funds) {
        return funds.stream()
                .map(this.calculator::calculateVendorRevenue)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalStateException::new);
    }

    public List<VendorTransaction> getByVendorId(Long id) {
        return this.manager.createNamedQuery(
                VendorTransaction.GET_BY_VENDOR_ID,
                VendorTransaction.class
        ).setParameter("vendorId", id)
                .getResultList();
    }

    public List<VendorTransaction> getByPeriod(Date beginDate, Date endDate) {
        return this.manager.createNamedQuery(
                VendorTransaction.GET_BY_PERIOD,
                VendorTransaction.class
        ).setParameter("beginDate", beginDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<VendorTransaction> getByType(TransactionType type) {
        return this.manager.createNamedQuery(
                VendorTransaction.GET_BY_TYPE,
                VendorTransaction.class
        ).setParameter("type", type)
                .getResultList();
    }

    public List<VendorTransaction> getByStatus(TransactionStatus status) {
        return this.manager.createNamedQuery(
                VendorTransaction.GET_BY_STATUS,
                VendorTransaction.class
        ).setParameter("status", status)
                .getResultList();
    }
}
