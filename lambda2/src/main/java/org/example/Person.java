package org.example;


import lombok.Data;

@Data
@Table(name = "person", alias = "p")
public class Person {
    private Long id;
    private String firstName;
    private String lastName;

    @Column("id") // 显式指定列名
    public Long getId() { return id; }
    
    // 省略其他 getter/setter
}