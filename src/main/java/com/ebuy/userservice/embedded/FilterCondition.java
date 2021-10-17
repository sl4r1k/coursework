package com.ebuy.userservice.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterCondition {
    IS_EQUAL("="),
    IS_NOT_EQUAL("<>"),
    IS_GREATER_OR_EQUAL(">="),
    IS_LESS_OR_EQUAL("<="),
    IS_GREATER(">"),
    IS_LESS("<"),
    IS_CONTAIN("IN"),
    IS_NOT_CONTAIN("NOT IN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL");

    private final String operator;
}
