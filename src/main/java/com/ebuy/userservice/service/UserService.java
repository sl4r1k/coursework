package com.ebuy.userservice.service;

import com.ebuy.userservice.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService extends Dao<User> {
    public UserService() {
        super(User.class);
    }

    public List<User> getByLogin(String login) {
        return this.manager.createNamedQuery(
                User.GET_BY_LOGIN,
                User.class
        ).setParameter("login", login)
                .getResultList();
    }

    public List<User> getByFullName(String fullName) {
        return this.manager.createNamedQuery(
                User.GET_BY_FULL_NAME,
                User.class
        ).setParameter("fullName", fullName)
                .getResultList();
    }

    public List<User> getByPhoneNumber(String phoneNumber) {
        return this.manager.createNamedQuery(
                User.GET_BY_PHONE_NUMBER,
                User.class
        ).setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    public List<User> getByFaxNumber(String faxNumber) {
        return this.manager.createNamedQuery(
                User.GET_BY_FAX_NUMBER,
                User.class
        ).setParameter("faxNumber", faxNumber)
                .getResultList();
    }

    public List<User> getByEmailAddress(String emailAddress) {
        return this.manager.createNamedQuery(
                User.GET_BY_EMAIL_ADDRESS,
                User.class
        ).setParameter("emailAddress", emailAddress)
                .getResultList();
    }

    public List<User> getByCompanyName(String companyName) {
        return this.manager.createNamedQuery(
                User.GET_BY_COMPANY_NAME,
                User.class
        ).setParameter("companyName", companyName)
                .getResultList();
    }

    public List<User> getByIndividualTaxpayerNumber(String individualTaxpayerNumber) {
        return this.manager.createNamedQuery(
                User.GET_BY_INDIVIDUAL_TAXPAYER_NUMBER,
                User.class
        ).setParameter("individualTaxpayerNumber", individualTaxpayerNumber)
                .getResultList();
    }

    public List<User> getByBankCardNumber(String bankCardNumber) {
        return this.manager.createNamedQuery(
                User.GET_BY_BANK_CARD_NUMBER,
                User.class
        ).setParameter("bankCardNumber", bankCardNumber)
                .getResultList();
    }

    public List<User> getByCriteriaId(Long id) {
        return this.getByCriteria(manager.find(UserSearchCriteria.class, id));
    }

    public List<User> getByCriteria(UserSearchCriteria criteria) {
        this.saveCriteriaIfNecessary(criteria);
        return this.manager.createNamedQuery(
                User.GET_BY_CRITERIA,
                User.class
        ).setParameter("login", criteria.getLogin())
                .setParameter("accountType", criteria.getAccountType())
                .setParameter("fullName", criteria.getFullName())
                .setParameter("companyName", criteria.getCompanyName())
                .setParameter("roleId", criteria.getRoleId())
                .setParameter("phoneNumber", criteria.getPhoneNumber())
                .setParameter("faxNumber", criteria.getFaxNumber())
                .setParameter("emailAddress", criteria.getEmailAddress())
                .setParameter("urlAddress", criteria.getUrlAddress())
                .setParameter("individualTaxpayerNumber", criteria.getIndividualTaxpayerNumber())
                .setParameter("bankCardNumber", criteria.getBankCardNumber())
                .setParameter("postalCode", criteria.getPostalCode())
                .setParameter("country", criteria.getCountry())
                .setParameter("region", criteria.getRegion())
                .setParameter("city", criteria.getCity())
                .setParameter("street", criteria.getStreet())
                .setParameter("houseNumber", criteria.getHouseNumber())
                .setParameter("tags", criteria.getTags())
                .setParameter("beginRegistrationDate", criteria.getBeginRegistrationDate())
                .setParameter("endRegistrationDate", criteria.getEndRegistrationDate())
                .setParameter("productIds", criteria.getOrderedProductIds())
                .setParameter("groupIds", criteria.getGroupIds())
                .setParameter("segmentIds", criteria.getSegmentIds())
                .getResultList();
    }

    private void saveCriteriaIfNecessary(UserSearchCriteria criteria) {
        if (Objects.nonNull(criteria.getTitle()) && !criteria.getTitle().isEmpty()) {
            this.manager.persist(criteria);
        }
    }

    public void saveRole(Role role) {
        this.manager.persist(role);
    }

    public void updateRoleByRoleId(Long roleId, Role newRole) {
        newRole.setId(roleId);
        this.manager.merge(newRole);
    }

    public List<Role> getAllRoles() {
        return this.manager.createNamedQuery(
                Role.GET_ALL,
                Role.class
        ).getResultList();
    }

    public Role getRoleByRoleId(Long roleId) {
        return this.manager.createNamedQuery(
                Role.GET_BY_ID,
                Role.class
        ).setParameter("id", roleId)
                .getSingleResult();
    }
}
