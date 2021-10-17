package com.ebuy.userservice.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum UserField {
    LOGIN(
            "users.login",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    ACCOUNT_TYPE(
            "users.account_type",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    LAST_NAME(
            "users.last_name",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    FIRST_NAME(
            "users.first_name",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    COMPANY_NAME(
            "users.company_name",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    ROLE(
            "users.role",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    PHONE_NUMBER(
            "users.phone_number",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    FAX_NUMBER(
            "users.fax_number",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    EMAIL_ADDRESS(
            "users.email_address",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    URL_ADDRESS(
            "users.url_address",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    INDIVIDUAL_TAXPAYER_NUMBER(
            "users.individual_taxpayer_number",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    BANK_CARD_NUMBER(
            "users.bank_card_number",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    POSTAL_CODE(
            "users.postal_code",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    COUNTRY(
            "users.country",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    REGION(
            "users.region",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    CITY(
            "users.city",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    STREET(
            "users.street",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    HOUSE_NUMBER(
            "users.house_number",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    TAGS(
            "(SELECT text"
                    + " FROM user_tags"
                    + " WHERE user_tags.user_id = users.id)",
            List.of(
                    FilterCondition.IS_CONTAIN,
                    FilterCondition.IS_NOT_CONTAIN
            ),
            true
    ),
    LAST_LOGIN_DATE(
            "users.last_login_date",
            List.of(
                    FilterCondition.IS_GREATER_OR_EQUAL,
                    FilterCondition.IS_LESS_OR_EQUAL,
                    FilterCondition.IS_GREATER,
                    FilterCondition.IS_LESS
            ),
            false
    ),
    REGISTRATION_DATE(
            "users.registration_date",
            List.of(
                    FilterCondition.IS_GREATER_OR_EQUAL,
                    FilterCondition.IS_LESS_OR_EQUAL,
                    FilterCondition.IS_GREATER,
                    FilterCondition.IS_LESS
            ),
            false
    ),
    AVAILABILITY(
            "users.availability",
            List.of(
                    FilterCondition.IS_EQUAL,
                    FilterCondition.IS_NOT_EQUAL
            ),
            false
    ),
    GROUPS(
            "(SELECT title"
                    + " FROM user_groups"
                    + " WHERE user_groups.user_id = users.id)",
            List.of(
                    FilterCondition.IS_CONTAIN,
                    FilterCondition.IS_NOT_CONTAIN
            ),
            true
    ),
    ORDERS_NUMBER(
            "(WITH groups AS ("
                    + "   SELECT DISTINCT user_ordered_products.date"
                    + " ) SELECT count(*) FROM groups)",
            List.of(
                    FilterCondition.IS_GREATER_OR_EQUAL,
                    FilterCondition.IS_LESS_OR_EQUAL,
                    FilterCondition.IS_GREATER,
                    FilterCondition.IS_LESS
            ),
            false
    ),
    TOTAL_AMOUNT(
            "(SELECT sum(user_ordered_products.price)"
                    + " FROM user_ordered_products"
                    + " WHERE user_ordered_products.user_id = users.id)",
            List.of(
                    FilterCondition.IS_GREATER_OR_EQUAL,
                    FilterCondition.IS_LESS_OR_EQUAL,
                    FilterCondition.IS_GREATER,
                    FilterCondition.IS_LESS
            ),
            false
    ),
    LAST_ORDER_DATE(
            "(SELECT user_ordered_products.date"
                    + " FROM user_ordered_products"
                    + " WHERE user_ordered_products.user_id = users.id"
                    + " ORDER BY date DESC"
                    + " LIMIT 1)",
            List.of(
                    FilterCondition.IS_GREATER_OR_EQUAL,
                    FilterCondition.IS_LESS_OR_EQUAL,
                    FilterCondition.IS_GREATER,
                    FilterCondition.IS_LESS
            ),
            false
    );

    private final String name;
    private final List<FilterCondition> applicableConditions;
    private final boolean isReturnValueArray;
}
