package com.sheryians.major.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "order_completed")
public class OrderCompleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String postcode;
    private String additionalInfo;
    private double amount;

    @OneToMany(mappedBy = "orderCompleted", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;



    // Default Constructor
    public OrderCompleted() {}

    // Constructor that accepts an Order
    public OrderCompleted(Order order) {
        this.id = order.getId();  // Remove if ID should be auto-generated
        this.firstName = order.getFirstName();
        this.lastName = order.getLastName();
        this.email = order.getEmail();
        this.phone = order.getPhone();
        this.city = order.getCity();
        this.addressLine1 = order.getAddressLine1();
        this.addressLine2 = order.getAddressLine2();
        this.postcode = order.getPostcode();
        this.additionalInfo = order.getAdditionalInfo();
        this.amount = order.getAmount();

        // Copy order items
        this.orderItems = order.getOrderItems().stream().map(item -> {
            OrderItem newItem = new OrderItem();
            newItem.setProductName(item.getProductName());
            newItem.setQuantity(item.getQuantity());
            newItem.setPrice(item.getPrice());
            newItem.setOrderCompleted(this); // Associate with completed order
            return newItem;
        }).collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
