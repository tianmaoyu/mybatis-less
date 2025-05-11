package org.deml;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class User {
    @Column(name = "user_age")
    private Integer age;

    @Column(name = "user_name")
    private String name;

    @Column(name = "score")
    private Integer score;

    @Column(name = "status")
    private Integer status;

    @Column(name = "vip_level")
    private Integer vipLevel;

    // Getter/Setter...
}