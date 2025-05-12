package org.lambda;
import jakarta.persistence.Column;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "person") // 指定表名
public class Person {
    @Column(name = "id") // 指定列名（可选）
    private Integer id;
    @Column(name = "name")
    private String name;
    
    // 省略 getter/setter
}