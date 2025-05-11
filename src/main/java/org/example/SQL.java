package org.example;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Function;

public class SQL {
    public static <T> QueryBuilder<T> selectFrom(Class<T> entityClass) {
        return new QueryBuilder<>(entityClass);
    }
    // 快速创建字段条件（如 (User::getName).like("A%")）
    public static <T> FieldCondition<T> Condition(FieldGetter<T> field) {
        return new FieldCondition<>(field);
    }

    // 快速创建字段条件（如 (User::getName).like("A%")）
    public static <T> FieldCondition<T> Condition(FieldGetter<T> field) {
        return new FieldCondition<>(field);
    }

    // region Insert
    public static <T> InsertBuilder<T> insert(T entity) {
        return new InsertBuilder<>(entity);
    }

    public static <T> InsertIntoBuilder<T> insertInto(Class<T> clazz) {
        return new InsertIntoBuilder<>(clazz);
    }

    public static <T> BatchInsertBuilder<T> insertBatch(Class<T> clazz) {
        return new BatchInsertBuilder<>(clazz);
    }
    // endregion

    @SafeVarargs
    public static <T> QueryBuilder<T> select(SFunction<T, ?>... columns) {
        return new QueryBuilder<T>(columns);
    }

    // region Update
    public static <T> UpdateBuilder<T> update(T entity) {
        return new UpdateBuilder<>(entity);
    }

    public static <T> UpdateBuilder<T> update(Class<T> clazz) {
        return new UpdateBuilder<>(clazz);
    }

    public static <T> BatchUpdateBuilder<T> updateBatch(Class<T> clazz) {
        return new BatchUpdateBuilder<>(clazz);
    }
    // endregion

    // region Delete
    public static <T> DeleteBuilder<T> deleteFrom(Class<T> clazz) {
        return new DeleteBuilder<>(clazz);
    }
    // endregion

    // 添加静态方法用于创建条件
    public static <T> ConditionBuilder<T, ?> where(FieldGetter<T> field) {
        return new ConditionBuilder<>(new QueryBuilder<>(Object.class), FieldResolver.getColumnName(field));
    }


}