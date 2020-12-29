package com.rufino.server.service;

import com.rufino.server.model.Order;
import com.rufino.server.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }
}
