package org.example;

import java.util.ArrayList;
import java.util.List;

public class ConditionBuilder<T, R> {
    private final QueryBuilder<T> queryBuilder;
    private final String columnName;
    private Condition currentCondition;

    public ConditionBuilder(QueryBuilder<T> queryBuilder, String columnName) {
        this.queryBuilder = queryBuilder;
        this.columnName = columnName;
    }

    public ConditionBuilder<T, R> gt(R value) {
        currentCondition = new Condition(columnName, ">", value);
        return this;
    }

    public ConditionBuilder<T, R> eq(R value) {
        currentCondition = new Condition(columnName, "=", value);
        return this;
    }

    public ConditionBuilder<T, R> neq(R value) {
        currentCondition = new Condition(columnName, "!=", value);
        return this;
    }

    public ConditionBuilder<T, R> lt(R value) {
        currentCondition = new Condition(columnName, "<", value);
        return this;
    }

    public ConditionBuilder<T, R> le(R value) {
        currentCondition = new Condition(columnName, "<=", value);
        return this;
    }

    public ConditionBuilder<T, R> ge(R value) {
        currentCondition = new Condition(columnName, ">=", value);
        return this;
    }

    public ConditionBuilder<T, R> like(String value) {
        currentCondition = new Condition(columnName, "LIKE", value);
        return this;
    }


    public QueryBuilder<T> and() {
        if (currentCondition != null) {
            queryBuilder.and(currentCondition);
        }
        return queryBuilder;
    }

    public QueryBuilder<T> or() {
        if (currentCondition != null) {
            queryBuilder.or(currentCondition);
        }
        return queryBuilder;
    }
}