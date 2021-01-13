package com.rufino.server;

import com.rufino.server.model.Order;
import com.rufino.server.service.OrderService;

import org.hamcrest.core.Is;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderPutRequestTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderService orderService;

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM ORDERS");
    }

    @Test
    void itShouldUpdateOrder() throws Exception {
        JSONObject my_obj = new JSONObject();

        Order order1 = new Order();
        setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
        saveAndAssert(order1);

        Order order2 = new Order();
        setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
        saveAndAssert(order2, 1, 2);

        my_obj.put("customerId", "846e1a32-f831-4bee-a6bc-673b5f901d7b");
        my_obj.put("orderTotalValue", 5.99);
        my_obj.put("orderPaymentMethod", "cash");
        my_obj.put("orderNumber", 123123);

        mockMvc.perform(put("/api/v1/order/" + order1.getOrderId()).contentType(MediaType.APPLICATION_JSON)
                .content(my_obj.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customerId", Is.is("846e1a32-f831-4bee-a6bc-673b5f901d7b")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalValue", Is.is(5.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderPaymentMethod", Is.is("CASH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderNumber", Is.is(123123))).andExpect(status().isOk())
                .andReturn();

        my_obj = new JSONObject();
        my_obj.put("customerId", "cba3ff2e-3087-49bd-bc9b-285e809e7b32");

        mockMvc.perform(put("/api/v1/order/" + order1.getOrderId()).contentType(MediaType.APPLICATION_JSON)
                .content(my_obj.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customerId", Is.is("cba3ff2e-3087-49bd-bc9b-285e809e7b32")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalValue", Is.is(5.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderPaymentMethod", Is.is("CASH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderNumber", Is.is(123123))).andExpect(status().isOk())
                .andReturn();

        my_obj = new JSONObject();
        my_obj.put("orderTotalValue", 0.99);
        mockMvc.perform(put("/api/v1/order/" + order1.getOrderId()).contentType(MediaType.APPLICATION_JSON)
                .content(my_obj.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customerId", Is.is("cba3ff2e-3087-49bd-bc9b-285e809e7b32")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalValue", Is.is(0.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderPaymentMethod", Is.is("CASH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderNumber", Is.is(123123))).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void itShouldUpdatePartiallyOrder() throws Exception {
        JSONObject my_obj = new JSONObject();

        Order order1 = new Order();
        setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
        saveAndAssert(order1);

        Order order2 = new Order();
        setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
        saveAndAssert(order2, 1, 2);

        my_obj.put("customerId", "846e1a32");
        my_obj.put("orderTotalValue", 5.99);
        my_obj.put("orderPaymentMethod", "cash");
        my_obj.put("orderNumber", 123123);

        mockMvc.perform(put("/api/v1/order/" + order1.getOrderId()).contentType(MediaType.APPLICATION_JSON)
                .content(my_obj.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.customerId", Is.is("cba3ff2e-3087-49bd-bc9b-285e809e7b32")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalValue", Is.is(5.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderPaymentMethod", Is.is("CASH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderNumber", Is.is(123123))).andExpect(status().isOk())
                .andReturn();
    }

    private void saveAndAssert(Order order) {
        long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
        assertEquals(0, countBeforeInsert);
        orderService.saveOrder(order);
        long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
        assertEquals(1, countAfterInsert);
    }

    private void saveAndAssert(Order order, int before, int after) {
        long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
        assertEquals(before, countBeforeInsert);
        orderService.saveOrder(order);
        long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
        assertEquals(after, countAfterInsert);
    }

    private void setOrder(Order order, String customerId) {
        order.setCustomerId(customerId);
        order.setOrderPaymentMethod("credit_card");
        order.setOrderTotalValue(1.99f);
        order.setOrderNumber(123456);
    }
}
