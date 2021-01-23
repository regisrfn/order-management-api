package com.rufino.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import com.rufino.server.exception.ApiRequestException;
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
		Order order = new Order();
		setOrder(order, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
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

		Order order1 = new Order();
		setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order1);

		Order order2 = new Order();
		setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
		saveAndAssert(order2, 1, 2);

		ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(2);
	}

	//////////////////// GET ORDER BY ID/////////////////////////////////
	@Test
	void itShouldGetAnOrder() {
		Order order = new Order();
		setOrder(order, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order);

		assertThat(orderService.getOrderById(order.getOrderId())).isNotEqualTo(null);

		assertThat(orderService.getOrderById(order.getOrderId()).getCustomerId())
				.isEqualTo(UUID.fromString("cba3ff2e-3087-49bd-bc9b-285e809e7b32"));

		assertThat(orderService.getOrderById(order.getOrderId()).getOrderNumber()).isEqualTo(order.getOrderNumber());

		assertThat(orderService.getOrderById(order.getOrderId()).getOrderPaymentMethod().toString())
				.isEqualTo("CREDIT_CARD");

	}

	@Test
	void itShouldNotGetAnOrder() {
		Order order = new Order();
		setOrder(order, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order);

		assertThat(orderService.getOrderById(UUID.fromString("846e1a32-f831-4bee-a6bc-673b5f901d7b"))).isEqualTo(null);

	}

	//////////////////// DELETE ORDER BY ID/////////////////////////////////
	@Test
	void itShouldDeleteOrderById() {

		Order order1 = new Order();
		setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order1);

		Order order2 = new Order();
		setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
		saveAndAssert(order2, 1, 2);

		List<Order> ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(2);

		assertThat(orderService.deleteOrderById(order1.getOrderId())).isEqualTo(1);

		ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(1);

	}

	@Test
	void itShouldNotDeleteOrderById_orderNotFound() {

		Order order1 = new Order();
		setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order1);

		Order order2 = new Order();
		setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
		saveAndAssert(order2, 1, 2);

		List<Order> ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(2);

		assertThat(orderService.deleteOrderById(UUID.randomUUID())).isEqualTo(0);

		ordersList = orderService.getAllOrders();
		assertThat(ordersList.size()).isEqualTo(2);

	}

	//////////////////// UPDATE ORDER BY ID/////////////////////////////////
	@Test
	void itShouldUpdateOrder() {

		Order order1 = new Order();
		setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order1);

		Order order2 = new Order();
		setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
		saveAndAssert(order2, 1, 2);

		Order orderToUpdate = new Order();
		orderToUpdate.setCustomerId("846e1a32-f831-4bee-a6bc-673b5f901d7b");
		orderToUpdate.setOrderPaymentMethod("cash");
		orderToUpdate.setOrderTotalValue(5.00f);
		orderToUpdate.setOrderDescription("Description test");
		orderToUpdate.setOrderNumber(654321);
		orderService.updateOrder(order1.getOrderId(), orderToUpdate);

		assertThat(orderService.getOrderById(order1.getOrderId()).getCustomerId())
				.isEqualTo(UUID.fromString("846e1a32-f831-4bee-a6bc-673b5f901d7b"));
		assertThat(orderService.getOrderById(order1.getOrderId()).getOrderPaymentMethod().toString()).isEqualTo("CASH");
		assertThat(orderService.getOrderById(order1.getOrderId()).getOrderTotalValue()).isEqualTo(5.00f);
		assertThat(orderService.getOrderById(order1.getOrderId()).getOrderCreatedAt())
				.isEqualTo(orderToUpdate.getOrderCreatedAt());
		assertThat(orderService.getOrderById(order1.getOrderId()).getOrderDescription())
				.isEqualTo(orderToUpdate.getOrderDescription());
		assertThat(orderService.getOrderById(order1.getOrderId()).getOrderNumber())
				.isEqualTo(orderToUpdate.getOrderNumber());

	}

	@Test
	void itShouldNotUpdateOrder_customerNotExists() {
		Order order1 = new Order();
		setOrder(order1, "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
		saveAndAssert(order1);

		Order order2 = new Order();
		setOrder(order2, "846e1a32-f831-4bee-a6bc-673b5f901d7b");
		saveAndAssert(order2, 1, 2);

		Order orderToUpdate = order1;
		orderToUpdate.setCustomerId("c6586b2e-a943-481f-a4e3-e768aff9e029");
		try {
			orderService.updateOrder(order1.getOrderId(), orderToUpdate);
			assert (false);
		} catch (ApiRequestException e) {
			// TODO: handle exception
		}
	}

	////////////////////////////////////////////////////////////////////////////////////
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
		order.setOrderNumber((int)Math.floor(100000+Math.random()*900000));
	}

}
