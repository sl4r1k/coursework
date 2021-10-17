package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.TransactionStatus;
import com.ebuy.userservice.embedded.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@NamedQuery(
        name = VendorTransaction.UPDATE_STATUS_BY_ID,
        query = "UPDATE VendorTransaction AS tx"
                + " SET tx.status = :newStatus"
                + " WHERE tx.id = :id"
)
@NamedQuery(
        name = VendorTransaction.GET_BY_VENDOR_ID,
        query = "SELECT tx"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.vendor.id = :vendorId"
)
@NamedQuery(
        name = VendorTransaction.GET_BY_PERIOD,
        query = "SELECT tx"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.date BETWEEN :beginDate AND :endDate"
)
@NamedQuery(
        name = VendorTransaction.GET_BY_TYPE,
        query = "SELECT tx"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.type = :type"
)
@NamedQuery(
        name = VendorTransaction.GET_BY_STATUS,
        query = "SELECT tx"
                + " FROM VendorTransaction AS tx"
                + " WHERE tx.status = :status"
)
@Table(name = "vendor_transactions")
@Entity
public class VendorTransaction extends IdentifiableEntity {
    public static final String UPDATE_STATUS_BY_ID = "VendorTransaction.updateStatusById";
    public static final String GET_BY_VENDOR_ID = "VendorTransaction.getByVendorId";
    public static final String GET_BY_PERIOD = "VendorTransaction.getByPeriod";
    public static final String GET_BY_TYPE = "VendorTransaction.getByType";
    public static final String GET_BY_STATUS = "VendorTransaction.getByStatus";

    @ManyToOne(fetch = FetchType.LAZY)
    User vendor;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    TransactionStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    Date date;

    @Column(name = "comment", length = 1000)
    String comment;
}
