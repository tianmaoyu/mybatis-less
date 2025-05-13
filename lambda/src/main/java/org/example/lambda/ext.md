解决深层嵌套 ，多层 or,and 嵌套  
public class SQL {
    // 其他原有逻辑...

    public SQL andGroup(Consumer<SQL> group) {
        // 记录当前条件栈深度，自动添加括号
        this.whereConditions.add("(");
        group.accept(this);
        this.whereConditions.add(")");
        return this;
    }

    public SQL orGroup(Consumer<SQL> group) {
        // 类似 andGroup，但用 OR 连接
        return this;
    }
}


        // 嵌套 AND 组
        and(sql -> {
            if (firstName != null) {
                sql.where(Person::getFirstName).like(firstName);
            }
            if (lastName != null) {
                sql.where(Person::getLastName).like(lastName);
            }
        });

        // 嵌套 OR 组
        or(sql -> {
            sql.where(Person::getAge).gt(18);
            sql.where(Person::getStatus).eq("ACTIVE");
        });

`java
and(g1 -> {
    g1.or(g2 -> { ... });
    g1.and(g3 -> { ... });
});

`

