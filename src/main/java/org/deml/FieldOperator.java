package org.deml;

public class FieldOperator {
    // 解析数值字段
    public static <T> NumericCondition<T> numeric(FieldGetter<T, Number> field) {
        return new NumericConditionImpl<>(field);
    }

    // 解析字符串字段
    public static <T> StringCondition<T> string(FieldGetter<T, String> field) {
        return new StringConditionImpl<>(field);
    }

    // 数值条件实现类
    private static class NumericConditionImpl<T> implements NumericCondition<T> {
        private final FieldGetter<T, Number> field;

        public NumericConditionImpl(FieldGetter<T, Number> field) {
            this.field = field;
        }

        @Override
        public Condition<T> gt(Number value) {
            return Condition.of(field, ">", value);
        }

        // 其他方法实现...
    }

    // 字符串条件实现类
    private static class StringConditionImpl<T> implements StringCondition<T> {
        private final FieldGetter<T, String> field;

        public StringConditionImpl(FieldGetter<T, String> field) {
            this.field = field;
        }

        @Override
        public Condition<T> like(String pattern) {
            return Condition.of(field, "LIKE", pattern);
        }

        // 其他方法实现...
    }
}