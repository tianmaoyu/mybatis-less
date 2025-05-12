package org.example.lambda;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class SQL<T> {
    private final Class<T> entityClass;
    private final StringBuilder sql = new StringBuilder();
    private final Map<String, Object> params = new LinkedHashMap<>(); // 参数键值对

    private SQL(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // 静态工厂方法
    public static <T> SQL<T> delete_from(Class<T> entityClass) {
        SQL<T> sqlBuilder = new SQL<>(entityClass);
        String tableName = getTableName(entityClass);
        sqlBuilder.sql.append("DELETE FROM ").append(tableName);
        return sqlBuilder;
    }

    // WHERE 条件（类型安全）
    public <R> Condition<T, R> where(SFunction<T, R> columnGetter) {
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


    private static <T, Object> String getColumnName2(SFunction<T, Object> columnGetter) {
        try {
            // 通过方法引用解析字段名（需要依赖字节码操作库如 ByteBuddy）
            String name = LambdaUtils.resolve(columnGetter);
            return name;
//            Column columnAnnotation = method.getAnnotation(Column.class);
//            return (columnAnnotation != null) ? columnAnnotation.name() : method.getName().replace("get", "").toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve column name", e);
        }
    }
    private static <T, R> String getColumnName(SFunction<T, R> columnGetter) {
        try {
            // 通过方法引用解析字段名（需要依赖字节码操作库如 ByteBuddy）
            String name = LambdaUtils.resolve(columnGetter);
            return name;
//            Column columnAnnotation = method.getAnnotation(Column.class);
//            return (columnAnnotation != null) ? columnAnnotation.name() : method.getName().replace("get", "").toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve column name", e);
        }
    }
    // 添加 SELECT 支持
    public static <T> SQL<T> select_from(Class<T> entityClass) {
        SQL<T> sqlBuilder = new SQL<>(entityClass);
        String tableName = getTableName(entityClass);
        sqlBuilder.sql.append("SELECT * FROM ").append(tableName);
        return sqlBuilder;
    }
    public SQL<T> insert_into() {
        String tableName = getTableName(entityClass);
        sql.append("INSERT INTO ").append(tableName);
        return this;
    }

    public <R> SQL<T> values(SFunction<T, R>... columnGetters) {
        sql.append(" (");
        for (SFunction<T,  R> getter : columnGetters) {
            String column = getColumnName(getter);
            sql.append(column).append(", ");
        }
        sql.setLength(sql.length() - 2); // 移除末尾 ", "
        sql.append(") VALUES (");
        // 值处理逻辑（略）
        return this;
    }

    // LEFT JOIN 支持
    public SQL<T> LEFT_JOIN(Class<?> joinClass) {
        String tableName = getTableName(joinClass);
        sql.append(" LEFT JOIN ").append(tableName);
        return this;
    }

    // ON 子句（用于 JOIN）
    public <R> SQL<T> ON(SFunction<T, R> columnGetter, SFunction<T, R> joinColumnGetter) {
        String columnName = getColumnName(columnGetter);
        String joinColumnName = getColumnName(joinColumnGetter);
        sql.append(" ON ").append(columnName).append(" = ").append(joinColumnName);
        return this;
    }
    public static <T> SQL<T> insert_into(Class<T> entityClass) {
        SQL<T> sqlBuilder = new SQL<>(entityClass);
        String tableName = getTableName(entityClass);
        sqlBuilder.sql.append("INSERT INTO ").append(tableName);
        return sqlBuilder;
    }

    public SQL<T> values(SFunction<T, Object> columnGetter, Object value) {
        String column = getColumnName2(columnGetter);
        params.put(column, value); // 存储参数
        return this;
    }

    // 批量设置多个列值
    public SQL<T> values(Consumer<ValueCollector<T>> consumer) {
        ValueCollector<T> collector = new ValueCollector<>(this);
        consumer.accept(collector);
        return this;
    }

    // AND 条件
    public <R> Condition<T, R> AND(SFunction<T, R> columnGetter) {
        sql.append(" AND ");
        return new Condition<>(this, getColumnName(columnGetter));
    }

    // OR 条件
    public <R> Condition<T, R> OR(SFunction<T, R> columnGetter) {
        sql.append(" OR ");
        return new Condition<>(this, getColumnName(columnGetter));
    }

    // 构建最终 SQL 和参数
    public String build() {
        if (sql.indexOf("INSERT INTO") != -1) {
            buildInsertClause();
        }
        return sql.toString();
    }
    // -------------------- 内部方法 --------------------
    private void buildInsertClause() {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            columns.append(entry.getKey()).append(", ");
            placeholders.append("#{").append(entry.getKey()).append("}, ");
        }
        columns.setLength(columns.length() - 2); // 移除末尾 ", "
        placeholders.setLength(placeholders.length() - 2);
        sql.append(" (").append(columns).append(") VALUES (").append(placeholders).append(")");
    }

    // -------------------- 内部类 --------------------
    public static class ValueCollector<T> {
        private final SQL<T> sqlBuilder;

        public ValueCollector(SQL<T> sqlBuilder) {
            this.sqlBuilder = sqlBuilder;
        }

        public ValueCollector<T> add(SFunction<T, Object> columnGetter, Object value) {
            sqlBuilder.values(columnGetter, value);
            return this;
        }
    }

    public Map<String, Object> getParams() {
        return params;
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