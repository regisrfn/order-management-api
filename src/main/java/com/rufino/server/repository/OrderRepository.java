package com.rufino.server.repository;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.JpaDao;
import com.rufino.server.dao.OrderDao;
import com.rufino.server.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private JpaDao jpaDataAccess;

    @Autowired
    public OrderRepository(JpaDao jpaDataAccess, JdbcTemplate jdbcTemplate) {
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
        Sort sort = Sort.by("orderCreatedAt").descending();
        return jpaDataAccess.findAll(sort);
    }

    @Override
    public Order getOrder(UUID id) {
        return jpaDataAccess.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(UUID id, Order order) {
        order.setOrderId(id);
        return jpaDataAccess.save(order);
    }

    @Override
    public Page<Order> getPage(int page, int size) {
        Sort sort = Sort.by("orderCreatedAt").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return jpaDataAccess.findAll(pageRequest);
    }
}
