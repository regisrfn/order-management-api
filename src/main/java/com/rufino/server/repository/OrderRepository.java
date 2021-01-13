package com.rufino.server.repository;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.dao.JpaDao;
import com.rufino.server.dao.OrderDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Order;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private JpaDao jpaDataAccess;
    private JdbcTemplate jdbcTemplate;
    private ObjectMapper om;

    @Autowired
    public OrderRepository(JpaDao jpaDataAccess, JdbcTemplate jdbcTemplate) {
        this.jpaDataAccess = jpaDataAccess;
        this.jdbcTemplate = jdbcTemplate;
        this.om = new ObjectMapper();

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
        String orderString;

        try {
            orderString = om.writeValueAsString(order);
            String sql = generateSqlUpdate(order, orderString);
            int result = jdbcTemplate.update(sql + "where order_id = ?", id);
            return (result > 0 ? getOrder(id) : null);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApiRequestException(e.getMessage());
        }
    }

    private String generateSqlUpdate(Order order, String orderString) throws JSONException {
        String sql = "UPDATE ORDERS SET ";
        JSONObject jsonObject = new JSONObject(orderString);
        Iterator<String> keys = jsonObject.keys();
        if (!keys.hasNext()) {
            throw new ApiRequestException("No valid data to update");
        }
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("orderDescription") || key.equals("customerId"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + jsonObject.get(key) + "' ";
            else if (key.equals("orderCreatedAt"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + order.getOrderCreatedAt().toString()
                        + "' ";
            else if (key.equals("orderId"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + order.getOrderId().toString()
                        + "' ";
            else if (key.equals("orderPaymentMethod"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "=" + order.getOrderPaymentMethod().ordinal()
                        + " ";
            else
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "=" + jsonObject.get(key) + " ";

            if (keys.hasNext()) {
                sql = sql + ", ";
            }
        }
        return sql;
    }
}
