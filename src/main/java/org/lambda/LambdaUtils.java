package org.lambda;

import org.example.SFunction;

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

    private static String resolveFieldName(String methodName) {
        if (methodName.startsWith("get")) {
            return methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            return methodName.substring(2);
        }
        throw new IllegalArgumentException("Invalid getter name: " + methodName);
    }
}