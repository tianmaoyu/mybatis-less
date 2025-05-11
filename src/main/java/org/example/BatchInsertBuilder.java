package org.example;

public class BatchInsertBuilder<T> {
    // 自动提取字段值
    public <V> BatchInsertBuilder<T> value(FieldGetter<T> fieldGetter) {
        return value(fieldGetter, entity -> resolveFieldValue(fieldGetter, entity));
    }

    // 固定值（所有记录相同）
    public <V> BatchInsertBuilder<T> value(FieldGetter<T> fieldGetter, V fixedValue) {
        return value(fieldGetter, entity -> fixedValue);
    }

    // 动态计算值（基于实体）
    public <V> BatchInsertBuilder<T> value(FieldGetter<T> fieldGetter, Function<T, V> valueExtractor) {
        String column = resolveColumnName(fieldGetter);
        fieldMappings.add(new FieldMapping<>(column, valueExtractor));
        return this;
    }

    private Object resolveFieldValue(FieldGetter<T> fieldGetter, T entity) {
        try {
            // 通过反射调用 Getter 方法（如 user.getName()）
            Method getter = resolveGetterMethod(fieldGetter);
            return getter.invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract field value", e);
        }
    }
}