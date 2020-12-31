package com.rufino.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.model.Order;
import com.rufino.server.service.OrderService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderGetRequestsTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM ORDERS");
    }

    @Test
    void itShouldGetAllOrders() throws Exception {
        JSONObject my_obj = new JSONObject();

        MvcResult result = mockMvc
                .perform(get("/api/v1/order/").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        List<Order> orderList = Arrays
                .asList(objectMapper.readValue(result.getResponse().getContentAsString(), Order[].class));

        assertThat(orderList.size()).isEqualTo(0);

        saveAndAssert(new Order(1.99f, "card", 123456));
        saveAndAssert(new Order(1.99f, "card", 1234567), 1, 2);

        result = mockMvc
                .perform(get("/api/v1/order/").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        orderList = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), Order[].class));

        assertThat(orderList.size()).isEqualTo(2);
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

}