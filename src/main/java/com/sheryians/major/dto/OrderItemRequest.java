package com.sheryians.major.dto;


import lombok.Data;

@Data
public class OrderItemRequest {
    private String productName;
    private int quantity;
    private double price;

    // Getters and setters omitted for brevity
}
