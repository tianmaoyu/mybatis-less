package org.example;

public class OrCondition extends Condition {
    private final Condition condition;

    public OrCondition(Condition condition) {
        super(condition.getColumnName(), "OR " + condition.getOperator(), condition.getValue());
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }
}