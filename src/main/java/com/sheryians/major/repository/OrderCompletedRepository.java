package com.sheryians.major.repository;

import com.sheryians.major.model.OrderCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCompletedRepository extends JpaRepository<OrderCompleted, Long> {
    List<OrderCompleted> findAll(); // Fetch all completed orders
}
