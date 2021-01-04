package com.rufino.server.dao;

import java.util.UUID;

import com.rufino.server.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDao extends JpaRepository<Order, UUID> {
    
}
