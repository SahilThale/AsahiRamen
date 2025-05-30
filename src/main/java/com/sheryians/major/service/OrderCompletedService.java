package com.sheryians.major.service;

import com.sheryians.major.model.OrderCompleted;
import com.sheryians.major.repository.OrderCompletedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCompletedService {

    private final OrderCompletedRepository orderCompletedRepository;

    public OrderCompletedService(OrderCompletedRepository orderCompletedRepository) {
        this.orderCompletedRepository = orderCompletedRepository;
    }

    public List<OrderCompleted> getAllCompletedOrders() {
        return orderCompletedRepository.findAll();
    }
}
