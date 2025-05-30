package com.sheryians.major.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String postcode;
    private String additionalInfo;
    private List<OrderItemRequest> orderItems;

    // Getters and setters omitted for brevity
}
