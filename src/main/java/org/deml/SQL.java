package org.deml;

public class SQL {
    // 查询入口：SELECT * FROM {table}
    public static <T,V> QueryBuilder<T,V> selectFrom(Class<T> entityClass) {
        return new QueryBuilder<T,V>(entityClass);
    }

    // 条件组合快捷方法（示例）
    public static <T> Condition<T> and(Condition<T> left, Condition<T> right) {
        return left.and(right);
    }

    public static <T> Condition<T> or(Condition<T> left, Condition<T> right) {
        return left.or(right);
    }
}