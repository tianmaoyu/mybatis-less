package org.deml;

public interface NumericCondition<T> {
    Condition<T> gt(Number value);
    Condition<T> lt(Number value);
    Condition<T> ge(Number value);
    Condition<T> le(Number value);
    Condition<T> eq(Number value);
}