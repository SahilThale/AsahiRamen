package com.sheryians.major.controller;

import com.sheryians.major.model.OrderCompleted;
import com.sheryians.major.repository.OrderCompletedRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderCompletedController {

    private final OrderCompletedRepository orderCompletedRepository;

    public OrderCompletedController(OrderCompletedRepository orderCompletedRepository) {
        this.orderCompletedRepository = orderCompletedRepository;
    }

    @GetMapping("/completed-orders")
    public String getCompletedOrders(Model model) {
        List<OrderCompleted> completedOrders = orderCompletedRepository.findAll();
        model.addAttribute("completedOrders", completedOrders);
        return "completed-orders";
    }
}
