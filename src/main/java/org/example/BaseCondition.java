package org.example;

// 基础条件接口（支持所有字段）
public interface BaseCondition<T> {
    Condition<T，V> eq(Object value);
    Condition<T> ne(Object value);
}

// 字符串类型条件接口（扩展基础接口，支持 LIKE）
public interface StringCondition<T> extends BaseCondition<T> {
    Condition<T> like(String pattern);
}

// 数值类型条件接口（扩展基础接口，支持 >, < 等）
public interface NumberCondition<T> extends BaseCondition<T> {
    Condition<T> gt(Number value);
    Condition<T> lt(Number value);
    Condition<T> ge(Number value);
    Condition<T> le(Number value);
}