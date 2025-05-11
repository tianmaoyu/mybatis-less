package org.example;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class Main {

    @Test
    public void insert() {
        User user = new User();
        user.setName("Alice");
        user.setAge(25);
        user.setId(1);

        // 或者更简化版本（自动提取非空字段）
        Integer row_count = SQL.insert(user).execute();

    }

    @Test
    public void insert_returnGeneratedKey() {
        User user = new User();
        user.setName("Alice");
        user.setAge(25);

        // 或者更简化版本（自动提取非空字段）
        Integer id = SQL.insert(user)
                .returnGeneratedKey()
                .execute();

    }

    @Test
    public void insertInto() {

        int row_count = SQL.insertInto(User.class)
                .value(User::getName, "Bob")
                .value(User::getAge, 30)
                .value(User::getId, 2)
                .execute();
    }
    @Test
    public void insertInto_returnGeneratedKey() {

        Long id = SQL.insertInto(User.class)
                .value(User::getName, "Bob")
                .value(User::getAge, 30)
                .returnGeneratedKey()  // 生成 SQL：INSERT INTO ... RETURNING id
                .execute();
    }

    @Test
    public void insertBatch() {
        User user1 = new User(1, "Alice", 25);
        User user2 = new User(2, "Bob", 30);

        List<User> users = Arrays.asList(user1,user2);

        Integer[] rows = SQL.insertBatch(User.class)
                .batch(users)
                .execute();
    }


    @Test
    public void getList_selectFrom() {
        List<User> users = SQL.selectFrom(User.class)
                .where(User::getAge).gt(18)
                .orderBy(User::getName)
                .execute();
    }

    @Test
    public void getList_select() {

        List<User> users = SQL.select(User::getName, Order::getAmount)
                .from(User.class)
                .join(Order.class).on(User::getId, Order::getUserId)
                .where(User::getAge).gt(18);
    }



    @Test
    public void getList_selectFrom_join() {

        List<UserOrderDto> results = SQL.select(User::getName, Order::getAmount)
                .from(User.class)
                .join(Order.class).on(User::getId, Order::getUserId)
                .where(User::getAge).gt(18)
                .to(UserOrderDto.class)
                .execute();
    }




    @Test
    public void update() {
        User user = new User();
        boolean success = SQL.update(user)
                .set(User::getAge, user.getAge())
                .where(User::getId).eq(user.getId())
                .execute();
    }

    @Test
    public void update_where() {

        User user = new User();
        user.setId(1);
        user.setAge(26);

        boolean success = SQL.update(User.class)
                .set(User::getAge, user.getAge())
                .where(User::getId).eq(user.getId())
                .execute();

    }

    @Test
    public void delete() {
        boolean success = SQL.deleteFrom(User.class)
                .where(User::getId).eq(1)
                .execute();
    }



    @Test
    public void select_join() {

        List<UserOrderDto> results = SQL.select(User::getName, Order::getAmount)
                .from(User.class)
                .join(Order.class).on(User::getId, Order::getUserId)
                .where(User::getAge).gt(18)
                .into(UserOrderDto.class)  // 关键转换方法
                .execute();
    }

    @Test
    public void select_join_into() {

        List<UserOrderDto> results = SQL.select(User::getName, Order::getAmount)
                .from(User.class)
                .join(Order.class).on(User::getId, Order::getUserId)
                .where(User::getAge).gt(18)
                .into(UserOrderDto.class)
                .execute();
    }



    @Test
    public void updateBatch() {


        // 批量更新：将所有用户的 age 增加 1，name 设为 "NewName"
        List<User> users = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30)
        );

        int[] result = SQL.updateBatch(User.class)
                .batch(users)
                .set(User::getName, "NewName")
                .set(User::getAge, user -> user.getAge() + 1)
                .where(User::getId)
                .execute();

    }

    @Test
    public void update_setExpr() {
        List<User> users = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30)
        );

        int[] result = SQL.updateBatch(User.class)
                .batch(users)
                .set(User::getAge, user -> user.getAge() + 1)     // 应用层计算
                .setExpr(User::getPoints, SQL.plus(User::getPoints, 100)) // 数据库层计算
                .where(User::getId)
                .execute();
    }


    @Test
    public void selectFrom_and() {
// 生成 SQL：
// SELECT * FROM users
// WHERE age > 18
//   AND (name LIKE 'A%' OR score >= 90)
//   OR (status = 1 AND vip_level >= 3)

        List<User> users = SQL.selectFrom(User.class)
                .where(User::getAge).gt(18)
                .and(c-> c.field(User::getName).like("A%").or(User::getScore).ge(90))
                .or(c-> c.field(User::getStatus).eq(1).and(User::getVipLevel).ge(3))
                .execute();
    }

}