package com.ebuy.userservice.service;

import com.ebuy.userservice.embedded.*;
import com.ebuy.userservice.entity.User;
import com.ebuy.userservice.entity.VendorSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class VendorService extends Dao<User> {
    private final EmailService emailService;
    private final FundsCalculator calculator;

    @Autowired
    protected VendorService(EmailService emailService, FundsCalculator calculator) {
        super(User.class);
        this.emailService = emailService;
        this.calculator = calculator;
    }

    @Override
    public void deleteById(Long id) {
        manager.createQuery(
                "DELETE FROM User AS vendor"
                        + " WHERE vendor.role.title = 'VENDOR'"
                        + " AND vendor.id = :id"
        ).setParameter("id", id)
                .executeUpdate();
    }

    public void updateMerchandisesById(Long id, List<Long> merchandiseIds) {
        manager.createNamedQuery(
                User.UPDATE_VENDOR_MERCHANDISES_BY_ID
        ).setParameter("id", id)
                .setParameter("merchandiseIds", merchandiseIds)
                .executeUpdate();
    }

    public void updateBalanceByIdAndTransactionType(Long id, TransactionType type, BigDecimal amount) {
        if (type.equals(TransactionType.TRANSFER)) {
            this.increaseBalanceWithCommissionById(id, amount);
        } else {
            this.decreaseBalanceById(id, amount);
        }
    }

    public void increaseBalanceWithCommissionById(Long id, BigDecimal amount) {
        User vendor = this.getVendorWithBalanceAndCommissionAndFixedCommissionFlagById(id);
        BigDecimal newBalance = calculator.calculateIncreasedBalance(
                vendor.getVendorBalance(),
                amount,
                vendor.getVendorCommission(),
                vendor.getIsFixedCommission()
        );
        this.setAvailabilityToActiveIfBalanceHasBecomeGreaterThanZero(id, vendor.getVendorBalance(), newBalance);
        this.updateBalanceById(id, newBalance);
    }

    private User getVendorWithBalanceAndCommissionAndFixedCommissionFlagById(Long id) {
        return manager.createQuery(
                "SELECT new User("
                        + "   vendor.vendorBalance,"
                        + "   vendor.vendorCommission,"
                        + "   vendor.isFixedCommission"
                        + " )"
                        + " FROM User AS vendor"
                        + " WHERE vendor.role.title = 'VENDOR'"
                        + " AND vendor.id = :id",
                User.class
        ).setParameter("id", id)
                .getSingleResult();
    }

    private void setAvailabilityToActiveIfBalanceHasBecomeGreaterThanZero(Long id, BigDecimal currentBalance,
                                                                          BigDecimal newBalance) {
        if (Boolean.logicalAnd(
                calculator.isLessThanOrEqualToZero(currentBalance),
                calculator.isGreaterThanZero(newBalance)
        )) {
            this.updateAvailabilityById(id, Availability.ACTIVE);
        }
    }

    private void updateBalanceById(Long id, BigDecimal newBalance) {
        manager.createNamedQuery(
                User.UPDATE_VENDOR_BALANCE_BY_ID
        ).setParameter("id", id)
                .setParameter("newBalance", newBalance)
                .executeUpdate();
    }

    public void decreaseBalanceById(Long id, BigDecimal amount) {
        this.setAvailabilityToInactiveAndNotifyIfBalanceHasBecomeLessThanZero(id, amount);
        BigDecimal newBalance = calculator.calculateDecreasedBalance(this.getBalanceById(id), amount);
        this.updateBalanceById(id, newBalance);
    }

    private void setAvailabilityToInactiveAndNotifyIfBalanceHasBecomeLessThanZero(Long id, BigDecimal amount) {
        BigDecimal currentBalance = this.getBalanceById(id);
        if (Boolean.logicalAnd(
                !calculator.isGreaterThanZero(currentBalance),
                calculator.isLessThanOrEqualToZero(calculator.calculateDecreasedBalance(currentBalance, amount))
        )) {
            this.updateAvailabilityById(id, Availability.INACTIVE);
            emailService.sendToAddressByEventTemplate(
                    this.getEmailAddressById(id),
                    EmailEvent.VENDOR_AVAILABILITY_INACTIVATED_DUE_TO_NEGATIVE_BALANCE
            );
        }
    }

    private BigDecimal getBalanceById(Long id) {
        return manager.createQuery(
                "SELECT vendor.vendorBalance"
                        + " FROM User AS vendor"
                        + " WHERE vendor.role.title = 'VENDOR'"
                        + " AND vendor.id = :id",
                BigDecimal.class
        ).getSingleResult();
    }

    private String getEmailAddressById(Long id) {
        return manager.createQuery(
                "SELECT vendor.emailAddress"
                        + " FROM User AS vendor"
                        + " WHERE vendor.role.title = 'VENDOR'"
                        + " AND vendor.id = :id",
                String.class
        ).setParameter("id", id)
                .getSingleResult();
    }

    public void updateAvailabilityById(Long id, Availability newAvailability) {
        manager.createNamedQuery(
                User.UPDATE_VENDOR_AVAILABILITY_BY_ID
        ).setParameter("id", id)
                .setParameter("newAvailability", newAvailability)
                .executeUpdate();
        this.notifyIfInactivated(id, newAvailability);
    }

    private void notifyIfInactivated(Long id, Availability newAvailability) {
        if (newAvailability.equals(Availability.INACTIVE)) {
            emailService.sendToAddressByEventTemplate(
                    this.getEmailAddressById(id),
                    EmailEvent.VENDOR_AVAILABILITY_INACTIVATED
            );
        }
    }

    public List<User> getByFullName(String fullName) {
        return manager.createNamedQuery(
                User.GET_VENDOR_BY_FULL_NAME,
                User.class
        ).setParameter("fullName", fullName)
                .getResultList();
    }

    public List<User> getByCompanyName(String companyName) {
        return manager.createNamedQuery(
                User.GET_VENDOR_BY_COMPANY_NAME,
                User.class
        ).setParameter("companyName", companyName)
                .getResultList();
    }

    public List<User> getByPhoneNumber(String phoneNumber) {
        return manager.createNamedQuery(
                User.GET_VENDOR_BY_PHONE_NUMBER,
                User.class
        ).setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    public List<User> getByEmailAddress(String emailAddress) {
        return manager.createNamedQuery(
                User.GET_VENDOR_BY_EMAIL_ADDRESS,
                User.class
        ).setParameter("emailAddress", emailAddress)
                .getResultList();
    }

    public List<User> getByFaxNumber(String faxNumber) {
        return manager.createNamedQuery(
                User.GET_VENDOR_BY_FAX_NUMBER,
                User.class
        ).setParameter("faxNumber", faxNumber)
                .getResultList();
    }

    public List<User> getByCriteriaId(Long id) {
        return this.getByCriteria(manager.find(VendorSearchCriteria.class, id));
    }

    public List<User> getByCriteria(VendorSearchCriteria criteria) {
        this.saveCriteriaIfNecessary(criteria);
        return manager.createNamedQuery(
                User.GET_VENDOR_BY_CRITERIA,
                User.class
        ).setParameter("fullName", criteria.getFullName())
                .setParameter("companyName", criteria.getCompanyName())
                .setParameter("phoneNumber", criteria.getPhoneNumber())
                .setParameter("emailAddress", criteria.getEmailAddress())
                .setParameter("faxNumber", criteria.getFaxNumber())
                .setParameter("merchandiseIds", criteria.getMerchandiseIds())
                .setParameter("orderIds", criteria.getOrderIds())
                .getResultList();
    }

    private void saveCriteriaIfNecessary(VendorSearchCriteria criteria) {
        if (Boolean.logicalAnd(
                Objects.nonNull(criteria.getTitle()),
                !criteria.getTitle().isEmpty()
        )) {
            this.manager.persist(criteria);
        }
    }
}
