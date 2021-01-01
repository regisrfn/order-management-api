package com.rufino.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import com.rufino.server.model.Order;
import com.rufino.server.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionSystemException;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	private OrderService orderService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void clearTable() {
		jdbcTemplate.update("DELETE FROM orders");
	}
	//////////////////// SAVE ORDER/////////////////////////////////

	@Test
	void itShouldSaveIntoDb() {
		Order order = new Order("cba3ff2e-3087-49bd-bc9b-285e809e7b32", 1.99f, "card", 123456);
		saveAndAssert(order);
	}

	@Test
	void itShouldNotSaveIntoDb() {
		try {
			Order order = new Order();
			saveAndAssert(order);
			assert (false);
		} catch (TransactionSystemException e) {
			assertNotNull(e);
		}
	}

	//////////////////// GET ALL ORDERS/////////////////////////////////
	@Test
	void itShouldGetAllOrders() {
		List<Order> ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(0);
		saveAndAssert(new Order("cba3ff2e-3087-49bd-bc9b-285e809e7b32", 1.99f, "card", 123456));
		saveAndAssert(new Order("846e1a32-f831-4bee-a6bc-673b5f901d7b", 1.99f, "card", 123456), 1, 2);
		ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(2);
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
