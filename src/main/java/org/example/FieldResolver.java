package org.example;

import jakarta.persistence.Column;

import java.lang.reflect.Field;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class FieldResolver {
    // 新增方法：通过反射解析 FieldGetter 对应的字段
    static Field resolveFieldFromGetter(FieldGetter<?, ?> getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(getter);
            String methodName = lambda.getImplMethodName();

            Class<?> clazz = Class.forName(lambda.getFunctionalInterfaceClass());
            return clazz.getDeclaredField(resolveFieldName(methodName));
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve field from getter", e);
        }
    }

    // 从 getter 方法名解析字段名（如 "getName" → "name"
    private static String resolveFieldName(String methodName) {
        if (methodName.startsWith("get")) {
            return methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            return methodName.substring(2);
        }
        throw new IllegalArgumentException("Invalid getter method: " + methodName);
    }

    public static String getColumnName(FieldGetter<?, ?> getter) {
        try {
            // 通过反射获取字段上的 @Column 注解
            Field field = resolveFieldFromGetter(getter);
            Column column = field.getAnnotation(Column.class);
            return (column != null && !column.name().isEmpty()) 
                ? column.name() 
                : getter.getFieldName();
        } catch (Exception e) {
            return getter.getFieldName();
        }
    }
}