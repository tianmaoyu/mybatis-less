package org.deml;

import java.util.List;

public class Condition<T> {
    private final String expression;
    private final List<Object> parameters;
    private LogicOperator operator;

    // 私有构造方法，确保通过工厂方法创建
    private Condition(String expression, List<Object> parameters, LogicOperator operator) {
        this.expression = expression;
        this.parameters = parameters;
        this.operator = operator;
    }

    // 静态方法：创建原子条件（如 age > ?）
    public static <T> Condition<T> of(FieldGetter<T, ?> field, String op, Object value) {
        String column = resolveColumnName(field);
        return new Condition<>(column + " " + op + " ?", List.of(value), null);
    }

    // 组合条件：AND
    public Condition<T> and(Condition<T> other) {
        return combine(other, LogicOperator.AND);
    }

    // 组合条件：OR
    public Condition<T> or(Condition<T> other) {
        return combine(other, LogicOperator.OR);
    }

    // 组合条件的核心逻辑（自动添加括号）
    private Condition<T> combine(Condition<T> other, LogicOperator operator) {
        String combinedExpr = "(" + this.expression + " " + operator + " " + other.expression + ")";
        List<Object> combinedParams = new ArrayList<>(this.parameters);
        combinedParams.addAll(other.parameters);
        return new Condition<>(combinedExpr, combinedParams, operator);
    }

    // Getters
    public String getExpression() { return expression; }
    public List<Object> getParameters() { return parameters; }
}