package org.example;

import lombok.Data;

@Data
public  class  OrderDto{
        private int id;
        private int amount;
        private int userId;
        private String userName;
        private String orderName;
    }


