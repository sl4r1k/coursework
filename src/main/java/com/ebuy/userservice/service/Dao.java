package com.ebuy.userservice.service;

import com.ebuy.userservice.entity.IdentifiableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public abstract class Dao<T extends IdentifiableEntity> {
    @PersistenceContext
    protected EntityManager manager;

    protected Class<T> type;

    @Autowired(required = false)
    protected Dao(Class<T> type) {
        this.type = type;
    }

    public void save(T entity) {
        this.manager.persist(entity);
    }

    public void updateById(Long id, T entity) {
        entity.setId(id);
        this.manager.merge(entity);
    }

    public void deleteById(Long id) {
        this.manager.remove(getById(id));
    }

    public List<T> getAll() {
        return this.manager.createQuery(
                String.format(
                        "SELECT entity FROM %s AS entity",
                        this.type.getName()
                ),
                this.type
        ).getResultList();
    }

    public T getById(Long id) {
        return this.manager.find(this.type, id);
    }
}
