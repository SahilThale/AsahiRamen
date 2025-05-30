package com.sheryians.major.service;

import com.sheryians.major.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByFirstName(String firstName);

    // Find orders by last name
    List<Order> findByLastName(String lastName);

    // Example: Find orders by full name (first name + last name)
    @Query("SELECT o FROM Order o WHERE CONCAT(o.firstName, ' ', o.lastName) = :fullName")
    List<Order> findByFullName(@Param("fullName") String fullName);

}
