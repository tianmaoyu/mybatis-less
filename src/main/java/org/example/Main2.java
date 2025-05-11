package org.example;

import java.util.List;


public class Main2 {
    public static void main(String[] args) {
        // 示例：查询年龄大于 18 的用户，按姓名排序
        List<User> users = SQL.selectFrom(User.class)
                .where(User::getAge).gt(18)
                .orderBy(User::getName)
                .execute();

    }
}