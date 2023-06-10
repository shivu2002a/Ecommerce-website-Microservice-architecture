package com.shivu.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivu.orderservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
