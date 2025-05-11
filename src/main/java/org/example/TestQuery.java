package org.example;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class TestQuery<T> {
    public <R> void eq(SFunction<T, R> column) {
        SerializedLambda lambda = this.parseLambda(column);
        String methodName = lambda.getImplMethodName();
        System.out.println(methodName);
    }

    private SerializedLambda parseLambda(Serializable serializable) {
        try {
            Method method = serializable.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(serializable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        TestQuery<User> query = new TestQuery<>();
        query.eq(User::getName);
    }
}