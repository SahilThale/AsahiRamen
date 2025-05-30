package com.sheryians.major.service;

import com.sheryians.major.model.Order;
import com.sheryians.major.model.OrderCompleted;
import com.sheryians.major.model.OrderItem;
import com.sheryians.major.repository.OrderCompletedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderCompletedRepository orderCompletedRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public List<Order> getCompletedOrders() {
        return orderRepository.findByStatus("Completed");
    }


    @Transactional
    public void markOrderComplete(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Cannot complete an order with no items.");
        }

        System.out.println("Completing Order ID: " + order.getId());
        System.out.println("Order Items Count: " + order.getOrderItems().size());

        OrderCompleted completedOrder = new OrderCompleted(order);

        completedOrder.setOrderItems(order.getOrderItems());

        orderCompletedRepository.save(completedOrder);

        orderRepository.delete(order);
    }



    @Transactional
    public void deleteOrder(long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }


    @Transactional
    public void saveOrder(Order order) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item.");
        }

        // Ensure each OrderItem has a reference to the parent Order
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        // Save the order (CascadeType.ALL ensures OrderItems are saved too)
        orderRepository.save(order);
    }

    /**
     * Marks an order as completed (Wrapper for markOrderComplete).
     */
    @Transactional
    public void markAsCompleted(Long orderId) {
        markOrderComplete(orderId);
    }

    public List<OrderCompleted> getAllCompletedOrders() {
        return orderCompletedRepository.findAll();
    }
}
