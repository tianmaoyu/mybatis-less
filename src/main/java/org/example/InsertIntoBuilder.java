package org.example;

import java.util.LinkedHashMap;
import java.util.Map;

// Insert 构建器示例
class InsertIntoBuilder<T> extends BaseBuilder<T> {
    private final Map<String, Object> valueMap = new LinkedHashMap<>();

    public InsertIntoBuilder(Class<T> clazz) {
        super(clazz);
    }

    public <V> InsertIntoBuilder<T> value(SFunction<T, V> column, Object value) {
        String columnName = LambdaUtils.resolve(column);
        valueMap.put(columnName, value);
        return this;
    }

    public ExecuteBuilder<Long> returnGeneratedKey() {
        return new ExecuteBuilder<>(this, true);
    }

    public Integer execute() {
        return new ExecuteBuilder<>(this, false).execute();
    }

    private class ExecuteBuilder<R> {
        // 实现 SQL 生成和执行逻辑
    }
}