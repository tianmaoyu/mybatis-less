package org.deml;

import java.util.ArrayList;
import java.util.List;

public class FieldCondition<T> {
    private final String column;
    private final Class<T> entityType;
    private String currentExpression;
    private List<Object> currentParams = new ArrayList<>();

    public FieldCondition(FieldGetter<T> fieldGetter) {
        this.column = resolveColumnName(fieldGetter);
        this.entityType = resolveEntityType(fieldGetter);
    }

    // 大于操作
    public FieldCondition<T> gt(Object value) {
        this.currentExpression = column + " > ?";
        this.currentParams.add(value);
        return this;
    }

    // LIKE 操作
    public FieldCondition<T> like(String pattern) {
        this.currentExpression = column + " LIKE ?";
        this.currentParams.add(pattern);
        return this;
    }

    // 等于操作
    public FieldCondition<T> eq(Object value) {
        this.currentExpression = column + " = ?";
        this.currentParams.add(value);
        return this;
    }

    // OR 连接另一个字段条件
    public FieldCondition<T> or(FieldGetter<T> field) {
        FieldCondition<T> nextCondition = new FieldCondition<>(field);
        this.currentExpression += " OR " + nextCondition.currentExpression;
        this.currentParams.addAll(nextCondition.currentParams);
        return this;
    }

    // AND 连接另一个字段条件
    public FieldCondition<T> and(FieldGetter<T> field) {
        FieldCondition<T> nextCondition = new FieldCondition<>(field);
        this.currentExpression += " AND " + nextCondition.currentExpression;
        this.currentParams.addAll(nextCondition.currentParams);
        return this;
    }

    // 获取最终条件和参数
    public Condition<T> build() {
        return new Condition<>("(" + currentExpression + ")", currentParams);
    }
}