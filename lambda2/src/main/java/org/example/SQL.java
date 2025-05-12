package org.example;

import org.example.SFunction;

public class SQL<T> {
    private final Class<T> entityClass;
    private final StringBuilder sql = new StringBuilder();

    public SQL(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // -------------------- 链式调用入口 --------------------
    public SQL<T> select(SFunction<T, ?>... columns) {
        sql.append("SELECT ");
        for (SFunction<T, ?> column : columns) {
            sql.append(resolveColumnName(column)).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length()); // 移除末尾 ", "
        return this; // 关键：返回当前对象以支持链式调用
    }

    public SQL<T> from() {
        String tableName = resolveTableName(entityClass);
        sql.append(" FROM ").append(tableName);
        return this;
    }

    // -------------------- 其他方法（WHERE、ORDER BY等） --------------------
    public ConditionBuilder<T> where(SFunction<T, ?> columnGetter) {
        String column = resolveColumnName(columnGetter);
        return new ConditionBuilder<>(this, column);
    }

    public  SQL<T> order_by(SFunction<T, ?> columnGetter) {
        String column = resolveColumnName(columnGetter);
        sql.append(" order by ").append(column);
        return  this;
    }

    // -------------------- 内部工具方法 --------------------
    private String resolveColumnName(SFunction<T, ?> columnGetter) {
        // 实现解析列名的逻辑（例如通过方法引用获取字段名）
        return "column_name"; // 示例值
    }

    private String resolveTableName(Class<T> entityClass) {
        // 实现解析表名的逻辑（例如通过注解获取）
        return "table_name"; // 示例值
    }

    @Override
    public String toString() {
        return sql.toString();
    }

    // -------------------- 条件构建器 --------------------
    public static class ConditionBuilder<T> {
        private final SQL<T> sqlBuilder;
        private final String column;

        public ConditionBuilder(SQL<T> sqlBuilder, String column) {
            this.sqlBuilder = sqlBuilder;
            this.column = column;
        }

        public SQL<T> eq(Object value) {
            sqlBuilder.sql.append(" WHERE ").append(column).append(" = #{").append(value).append("}");
            return sqlBuilder;
        }

        public SQL<T> like(Object value) {
            sqlBuilder.sql.append(" and  ").append(column).append(" like #{").append(value).append("}");
            return sqlBuilder;
        }
//        public SQL<T> orderBy(Object value) {
//            sqlBuilder.sql.append(" order  ").append(column).append(" by #{").append(value).append("}");
//            return sqlBuilder;
//        }
    }
}