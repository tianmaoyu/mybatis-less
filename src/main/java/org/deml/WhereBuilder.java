package org.deml;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class WhereBuilder<T> {
    private final List<Condition<T>> conditions = new ArrayList<>();
    private final List<Object> parameters = new ArrayList<>();

    // 起始条件（如 where(age).gt(18)）
    public WhereBuilder<T> where(FieldGetter<T, ?> field) {
        return new WhereBuilder<T>().and(field);
    }

    // 添加 AND 条件
    public WhereBuilder<T> and(FieldGetter<T, ?> field) {
        return addCondition(field, LogicOperator.AND);
    }

    // 添加 OR 条件
    public WhereBuilder<T> or(FieldGetter<T, ?> field) {
        return addCondition(field, LogicOperator.OR);
    }

    // 添加嵌套条件（如 .and(cond)）
    public WhereBuilder<T> and(Condition<T> condition) {
        conditions.add(condition);
        parameters.addAll(condition.getParameters());
        return this;
    }

    // 添加嵌套条件（如 .or(cond)）
    public WhereBuilder<T> or(Condition<T> condition) {
        conditions.add(condition);
        parameters.addAll(condition.getParameters());
        return this;
    }

    // 构建 WHERE 子句
    public String build() {
        if (conditions.isEmpty()) return "";
        StringJoiner joiner = new StringJoiner(" ");
        for (Condition<T> cond : conditions) {
            joiner.add(cond.getExpression());
        }
        return "WHERE " + joiner.toString();
    }

    // 私有方法：添加原子条件
    private WhereBuilder<T> addCondition(FieldGetter<T, ?> field, LogicOperator operator) {
        // 实际实现中需通过类型安全接口约束操作符
        // 此处简化为直接调用 Condition.of
        return this;
    }
}