package org.example;

public class Condition {
        private final String columnName;
        private final String operator;
        private final Object value;

        public Condition(String columnName, String operator, Object value) {
            this.columnName = columnName;
            this.operator = operator;
            this.value = value;
        }

        public String getColumnName() { return columnName; }
        public String getOperator() { return operator; }
        public Object getValue() { return value; }
    }