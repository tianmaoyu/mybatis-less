package org.example;

import lombok.Data;
import jakarta.persistence.Column;

@Data
public  class  Order{
        @Column(name = "id")
        private int id;
        
        @Column(name = "amount")
        private int amount;
        
        @Column(name = "user_id")
        private int userId;
        
        @Column(name = "user_name")
        private String userName;
        
        @Column(name = "order_name")
        private String orderName;
}