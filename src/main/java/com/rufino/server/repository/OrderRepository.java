package com.rufino.server.repository;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.JpaDao;
import com.rufino.server.dao.OrderDao;
import com.rufino.server.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private JpaDao jpaDataAccess;

    @Autowired
    public OrderRepository(JpaDao jpaDataAccess) {
        this.jpaDataAccess = jpaDataAccess;
    }

    @Override
    public Order insertOrder(Order order) {
        return jpaDataAccess.save(order);
    }

    @Override
    public int deleteOrder(UUID id) {
        try {
            jpaDataAccess.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Order> getAll() {
        return jpaDataAccess.findAll();
    }

    @Override
    public Order getOrder(UUID id) {
        return jpaDataAccess.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(UUID id, Order order) {
        // TODO Auto-generated method stub
        return null;
    }
}
