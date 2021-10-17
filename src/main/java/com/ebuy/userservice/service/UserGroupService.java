package com.ebuy.userservice.service;

import com.ebuy.userservice.entity.UserGroup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The user group management service.
 */
@Service
public class UserGroupService extends Dao<UserGroup> {
    protected UserGroupService() {
        super(UserGroup.class);
    }

    public List<UserGroup> getByTitle(String title) {
        return manager.createNamedQuery(
                UserGroup.GET_BY_TITLE,
                UserGroup.class
        ).setParameter("title", title)
                .getResultList();
    }
}
