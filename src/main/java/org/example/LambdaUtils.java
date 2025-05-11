package org.example;

import org.springframework.util.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

// Lambda 解析工具类
class LambdaUtils {
    public static <T, R> String resolve(SFunction<T, R> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
            String getter = lambda.getImplMethodName();
            return resolveFieldName(getter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String resolveFieldName(String getter) {
        if (getter.startsWith("get")) {
            return StringUtils.uncapitalize(getter.substring(3));
        } else if (getter.startsWith("is")) {
            return StringUtils.uncapitalize(getter.substring(2));
        }
        throw new IllegalArgumentException("Invalid getter name: " + getter);
    }
}