package org.deml;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T,R> {
    private final List<Condition<T>> conditions = new ArrayList<>();
    private final List<Object> allParams = new ArrayList<>();

    // 初始 WHERE 条件
    public QueryBuilder<T,R> where(FieldGetter<T,R> field) {
        return new QueryBuilder<T,R>().and(field);
    }

    // 添加 AND 条件
    public QueryBuilder<T,R> and(FieldGetter<T,R> field) {
        FieldCondition<T> condition = new FieldCondition<>(field);
        conditions.add(condition.build());
        allParams.addAll(condition.getParams());
        return this;
    }

    // 添加 OR 条件
    public QueryBuilder<T,R> or(FieldGetter<T,R> field) {
        FieldCondition<T> condition = new FieldCondition<>(field);
        conditions.add(condition.build());
        allParams.addAll(condition.getParams());
        return this;
    }

    // 执行查询
    public List<T> execute() {
        String sql = buildSql();
        return jdbcTemplate.query(sql, allParams, rowMapper);
    }

    // 构建 SQL
    private String buildSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName());
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            StringJoiner joiner = new StringJoiner(" ");
            for (Condition<T> cond : conditions) {
                joiner.add(cond.getExpression());
            }
            sql.append(joiner.toString().replaceFirst("AND |OR ", ""));
        }
        return sql.toString();
    }
}