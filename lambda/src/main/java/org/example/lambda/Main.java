package org.example.lambda;

public class Main {
    public static void main(String[] args) {
        // 示例 1：删除操作
        String deleteSql = SQL.delete_from(Person.class)
            .where(Person::getId).eq(1L)
            .toString();
        System.out.println(deleteSql); 


        // 示例 2：查询操作（需扩展 SELECT 方法）
        String selectSql = SQL.select_from(Person.class)
            .where(Person::getName).eq("Alice")
            .toString();

        System.out.println(selectSql);

        SQL<Person> insertSql = SQL.insert_into(Person.class)
                .values(Person::getName, "Alice")  // 类型安全！
                .values(Person::getAge, 30);       // 自动推导列名

        System.out.println(insertSql.build());
        System.out.println(insertSql.getParams());

        SQL<Person> insertSql2 = SQL.insert_into(Person.class)
                .values(collector -> collector
                        .add(Person::getName, "Bob")
                        .add(Person::getAge, 25)
                        .add(Person::getId, 1001)
                );

        System.out.println(insertSql2.build());
        // 输出: INSERT INTO person (name, age, id) VALUES (#{name}, #{age}, #{id})

        System.out.println(insertSql2.getParams());
        // 输出: {name=Bob, age=25, id=1001}


//        // 示例 3：LEFT JOIN 查询
//        String joinSql = SQL.SELECT_FROM(Person.class)
//            .LEFT_JOIN(Order.class)
//            .ON(Person::getId, Order::getPersonId)  // 修正方法名为 getPersonId
//            .WHERE(Person::getName).eq("Alice")
//            .AND(Person::getId).eq(1L)  // 使用 1L 表示 Long 类型
//            .toString();
//        System.out.println(joinSql);
        // 输出: SELECT * FROM person LEFT JOIN order ON id = person_id WHERE name = 'Alice' AND id = 1

    }
}