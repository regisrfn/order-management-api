package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.OrderDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public Order saveOrder(Order order) {
        return orderDao.insertOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderDao.getAll();
    }

    public Order getOrderById(UUID id) {
        return orderDao.getOrder(id);
    }

    public int deleteOrderById(UUID id) {
        return orderDao.deleteOrder(id);
    }

    public Order updateOrder(UUID id, Order order) {
        try {
            order.setOrderId(null);
            return orderDao.updateOrder(id, order);
        } catch (DataIntegrityViolationException e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
