package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.Order;
import com.rufino.server.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }
    
    public List<Order>getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(UUID id){
        return orderRepository.findById(id).orElse(null);
    }

}
