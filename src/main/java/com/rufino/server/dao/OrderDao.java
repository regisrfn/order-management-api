package com.rufino.server.dao;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.Order;

public interface OrderDao {
    Order insertOrder(Order order);

    int deleteOrder(UUID id);

    List<Order> getAll();

    Order getOrder(UUID id);

    Order updateOrder(UUID id, Order order);
}
