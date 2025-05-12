package org.lambda;

public class Main {
    public static void main(String[] args) {
        // 示例 1：删除操作
        String deleteSql = SQL.DELETE_FROM(Person.class)
            .WHERE(Person::getId).eq(1)
            .toString();
        System.out.println(deleteSql); 
        // 输出: DELETE FROM person WHERE id = 1

        // 示例 2：查询操作（需扩展 SELECT 方法）
        String selectSql = SQL.SELECT_FROM(Person.class)
            .WHERE(Person::getName).eq("Alice")
            .toString();
        System.out.println(selectSql); 
        // 输出: SELECT * FROM person WHERE name = 'Alice'
    }
}