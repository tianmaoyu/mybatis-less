package org.example;

public class FieldCondition<T> {
    private final FieldGetter<T, ?> fieldGetter;

    public FieldCondition(FieldGetter<T, ?> fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    public Condition like(String value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, "LIKE", value);
    }

    public Condition eq(Object value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, "=", value);
    }

    public Condition neq(Object value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, "!=", value);
    }

    public Condition gt(Object value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, ">", value);
    }

    public Condition ge(Object value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, ">=", value);
    }

    public Condition lt(Object value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, "<", value);
    }

    public Condition le(Object value) {
        String columnName = FieldResolver.getColumnName(fieldGetter);
        return new Condition(columnName, "<=", value);
    }
}