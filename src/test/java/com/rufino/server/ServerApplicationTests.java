package com.rufino.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

	@Test
	void itShouldSaveIntoDb() {
		Order order = new Order(18.99f, "Card", 123456);
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

	private void saveAndAssert(Order order) {
		long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(0, countBeforeInsert);
		orderService.saveOrder(order);
		long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(1, countAfterInsert);
	}

}
