package com.ebuy.userservice.service;

import com.ebuy.userservice.entity.SupplierSearchCriteria;
import com.ebuy.userservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SupplierService extends Dao<User> {
    protected SupplierService() {
        super(User.class);
    }

    @Override
    public void deleteById(Long id) {
        manager.createQuery(
                "DELETE FROM User AS supplier"
                        + " WHERE supplier.role.title = 'SUPPLIER'"
                        + " AND supplier.id = :id"
        ).setParameter("id", id)
                .executeUpdate();
    }

    public List<User> getByCriteriaId(Long id) {
        return this.getByCriteria(manager.find(SupplierSearchCriteria.class, id));
    }

    public List<User> getByCriteria(SupplierSearchCriteria criteria) {
        this.saveCriteriaIfNecessary(criteria);
        return manager.createNamedQuery(
                User.GET_SUPPLIER_BY_CRITERIA,
                User.class
        ).setParameter("fullName", criteria.getFullName())
                .setParameter("companyName", criteria.getCompanyName())
                .setParameter("phoneNumber", criteria.getPhoneNumber())
                .setParameter("emailAddress", criteria.getEmailAddress())
                .setParameter("faxNumber", criteria.getFaxNumber())
                .setParameter("merchandiseIds", criteria.getProductIds())
                .getResultList();
    }

    private void saveCriteriaIfNecessary(SupplierSearchCriteria criteria) {
        if (Boolean.logicalAnd(
                Objects.nonNull(criteria.getTitle()),
                !criteria.getTitle().isEmpty()
        )) {
            this.manager.persist(criteria);
        }
    }
}
