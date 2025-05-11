package org.deml;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
public interface FieldGetter<T, V> extends Function<T, V>, Serializable{
    // 定义如何从实体对象 T 中获取字段值 V
//    V get(T entity);

    // 可选：默认方法，用于解析字段名（需结合反射或注解处理器）
    default String getFieldName() {
        // 通过反射解析方法引用对应的字段名
        try {
            Method method = this.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(this);
            String methodName = lambda.getImplMethodName();
            // 从 getter 方法名解析字段名（如 "getName" → "name"）
            return resolveFieldName(methodName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve field name", e);
        }
    }

    private String resolveFieldName(String methodName) {
        if (methodName.startsWith("get")) {
            return Introspector.decapitalize(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return Introspector.decapitalize(methodName.substring(2));
        }
        throw new IllegalArgumentException("Invalid getter method: " + methodName);
    }
}