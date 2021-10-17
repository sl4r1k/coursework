package com.ebuy.userservice.service;

import com.ebuy.userservice.embedded.FilterCondition;
import com.ebuy.userservice.embedded.UserField;
import com.ebuy.userservice.entity.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * The user segment management service.
 */
@Service
public class UserSegmentService extends Dao<UserSegment> {
    public UserSegmentService() {
        super(UserSegment.class);
    }

    /**
     * Saves the segment and adds the filtered users to it.
     *
     * @param segment the saved segment
     */
    @Override
    public void save(UserSegment segment) {
        segment.addUsers(this.getUsersByFilters(segment.getFilters()));
        super.save(segment);
    }

    @SuppressWarnings("unchecked")
    private List<User> getUsersByFilters(List<UserSegmentFilter> filters) {
        TypedQuery<User> query = this.manager.createQuery(
                this.buildQueryString(filters),
                User.class
        );
        return this.setParametersByFilters(query, filters)
                .getResultList();
    }

    /**
     * Builds a query string based on filter parameters.
     *
     * @param filters the segment filters
     * @return the query string
     */
    private String buildQueryString(List<UserSegmentFilter> filters) {
        AtomicInteger valueNumber = new AtomicInteger();
        return "SELECT * FROM users WHERE "
                + filters.stream()
                .map(filter -> this.formatQuery(valueNumber.incrementAndGet(), filter))
                .collect(Collectors.joining(" AND "));
    }

    /**
     * Formats the query string.
     *
     * @param valueNumber the value number
     * @param filter      the filter
     * @return the formatted query
     * @throws UnsupportedOperationException if filter condition does not applicable to field
     */
    private String formatQuery(int valueNumber, UserSegmentFilter filter) {
        this.throwExceptionIfConditionDoesNotApplicableToField(filter.getField(), filter.getCondition());
        return String.format(
                this.getPatternByFilteringField(filter.getField()),
                filter.getField().getName(),
                filter.getCondition().getOperator(),
                valueNumber
        );
    }

    private void throwExceptionIfConditionDoesNotApplicableToField(UserField field, FilterCondition condition) {
        if (!field.getApplicableConditions().contains(condition)) {
            throw new UnsupportedOperationException(String.format(
                    "The condition \"%s\" does not applicable to the field \"%s\"",
                    condition.getOperator(),
                    field.getName()
            ));
        }
    }

    /**
     * Determines the desired pattern based on the value flag.
     *
     * @param field the user field
     * @return the query string pattern
     */
    private String getPatternByFilteringField(UserField field) {
        final String PATTERN_WITH_PARAMETER_AND_CONDITION_AND_VALUE = "(:?%d) %s %s";
        final String PATTERN_WITH_VALUE_AND_CONDITION_AND_PARAMETER = "%s %s (:?%d)";
        return field.isReturnValueArray()
                ? PATTERN_WITH_PARAMETER_AND_CONDITION_AND_VALUE
                : PATTERN_WITH_VALUE_AND_CONDITION_AND_PARAMETER;
    }

    /**
     * Updates segment filters and overwrites users with the new filter.
     *
     * @param id      the segment id
     * @param filters the updatable segment
     */
    public void updateFiltersById(Long id, List<UserSegmentFilter> filters) {
        UserSegment segment = this.getById(id);
        segment.setFilters(filters);
        this.overwriteUsers(filters, segment);
        this.updateById(id, segment);
    }

    private void overwriteUsers(List<UserSegmentFilter> filters, UserSegment segment) {
        segment.removeUsers();
        segment.addUsers(this.getUsersByFilters(filters));
    }

    /**
     * Adds a persistent or updatable user to the appropriate segment based on their updated information.
     *
     * @param user the persistent of updatable user
     */
    @PostUpdate
    @PostPersist
    private void addUserToSuitableSegments(User user) {
        user.removeSegments();
        this.getAll().stream()
                .filter(segment -> this.isUserContainedInFoundByFiltersUsers(user, segment.getFilters()))
                .peek(segment -> segment.addUser(user))
                .forEach(segment -> this.updateById(segment.getId(), segment));
    }

    private boolean isUserContainedInFoundByFiltersUsers(User user, List<UserSegmentFilter> filters) {
        return this.getUsersByFilters(filters).contains(user);
    }

    private Query setParametersByFilters(TypedQuery<User> query, List<UserSegmentFilter> filters) {
        for (int i = 0; i < filters.size(); i++) {
            UserSegmentFilter filter = filters.get(i);
            query.setParameter(i, this.getValueByCondition(filter.getCondition(), filter.getValues()));
        }
        return query;
    }

    private Object getValueByCondition(FilterCondition condition, List<String> values) {
        return Boolean.logicalOr(
                condition.equals(FilterCondition.IS_CONTAIN),
                condition.equals(FilterCondition.IS_NOT_CONTAIN)
        ) ? values : values.get(0);
    }

    public List<UserSegment> getByTitle(String title) {
        return manager.createNamedQuery(UserSegment.GET_BY_TITLE, UserSegment.class)
                .setParameter("title", title)
                .getResultList();
    }
}
