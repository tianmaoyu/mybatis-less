package org.example;

import lombok.Data;

@Data
public  class UserOrderDto {
        private String name;
        private int amount;

        public UserOrderDto(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }

        // getters and setters
    }