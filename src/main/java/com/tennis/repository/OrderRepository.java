package com.tennis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tennis.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}