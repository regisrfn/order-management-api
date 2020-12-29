package com.rufino.server.repository;

import java.util.UUID;

import com.rufino.server.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,UUID>{
}
