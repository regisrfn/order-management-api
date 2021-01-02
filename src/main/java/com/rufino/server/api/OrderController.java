package com.rufino.server.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Order;
import com.rufino.server.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
@CrossOrigin
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order saveOrder(@Valid @RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("{id}")
    public Order getOrderById(@PathVariable String id) {
        try {
            UUID orderId = UUID.fromString(id);
            Order order = orderService.getOrderById(orderId);
            if (order == null)
                throw new ApiRequestException("Order not found", HttpStatus.NOT_FOUND);
            return order;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid user UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public Map<String, String> deleteOrderById(@PathVariable String id) {
        Map<String, String> message = new HashMap<>();

        try {
            UUID orderId = UUID.fromString(id);
            int op = orderService.deleteOrderById(orderId);
            if (op == 0)
                throw new ApiRequestException("Order not found", HttpStatus.NOT_FOUND);
            message.put("message", "successfully operation");
            return message;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid user UUID format", HttpStatus.BAD_REQUEST);
        }
    }
}
