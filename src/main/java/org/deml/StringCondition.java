package org.deml;

public interface StringCondition<T> {
    Condition<T> like(String pattern);
    Condition<T> eq(String value);
}