package org.lambda;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.example.SFunction;

import java.lang.reflect.Method;
import java.util.function.Function;

public class SQL<T> {
    private final Class<T> entityClass;
    private final StringBuilder sql = new StringBuilder();

    private SQL(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // 静态工厂方法
    public static <T> SQL<T> DELETE_FROM(Class<T> entityClass) {
        SQL<T> sqlBuilder = new SQL<>(entityClass);
        String tableName = getTableName(entityClass);
        sqlBuilder.sql.append("DELETE FROM ").append(tableName);
        return sqlBuilder;
    }

    // WHERE 条件（类型安全）
    public <R> Condition<T, R> WHERE(Function<T, R> columnGetter) {
        String columnName = getColumnName(columnGetter);
        return new Condition<>(this, columnName);
    }

    // 构建最终 SQL
    @Override
    public String toString() {
        return sql.toString();
    }

    // -------------------- 内部工具方法 --------------------
    private static String getTableName(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return (tableAnnotation != null) ? tableAnnotation.name() : clazz.getSimpleName().toLowerCase();
    }

    private static <T, R> String getColumnName(SFunction<T, R> columnGetter) {
        try {
            // 通过方法引用解析字段名（需要依赖字节码操作库如 ByteBuddy）
            Method method = LambdaUtils.resolve(columnGetter);
            Column columnAnnotation = method.getAnnotation(Column.class);
            return (columnAnnotation != null) ? columnAnnotation.name() : method.getName().replace("get", "").toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve column name", e);
        }
    }
    // 添加 SELECT 支持
    public static <T> SQL<T> SELECT_FROM(Class<T> entityClass) {
        SQL<T> sqlBuilder = new SQL<>(entityClass);
        String tableName = getTableName(entityClass);
        sqlBuilder.sql.append("SELECT * FROM ").append(tableName);
        return sqlBuilder;
    }
    public SQL<T> INSERT_INTO() {
        String tableName = getTableName(entityClass);
        sql.append("INSERT INTO ").append(tableName);
        return this;
    }

    public SQL<T> VALUES(Function<T, Object>... columnGetters) {
        sql.append(" (");
        for (Function<T, Object> getter : columnGetters) {
            String column = getColumnName(getter);
            sql.append(column).append(", ");
        }
        sql.setLength(sql.length() - 2); // 移除末尾 ", "
        sql.append(") VALUES (");
        // 值处理逻辑（略）
        return this;
    }

    // -------------------- Condition 链式调用 --------------------
    public static class Condition<T, R> {
        private final SQL<T> sqlBuilder;
        private final String column;

        public Condition(SQL<T> sqlBuilder, String column) {
            this.sqlBuilder = sqlBuilder;
            this.column = column;
        }

        public SQL<T> eq(R value) {
            sqlBuilder.sql.append(" WHERE ").append(column).append(" = ").append(formatValue(value));
            return sqlBuilder;
        }

        public SQL<T> ne(R value) {
            sqlBuilder.sql.append(" WHERE ").append(column).append(" != ").append(formatValue(value));
            return sqlBuilder;
        }

        private String formatValue(R value) {
            // 如果是字符串则加单引号，数字直接使用
            return (value instanceof String) ? "'" + value + "'" : value.toString();
        }
    }
}