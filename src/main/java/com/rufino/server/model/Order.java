package com.rufino.server.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="orders")
public class Order {

    @Id
    private UUID orderId;

    @NotNull(message = "Value should not be empty")
    private Float orderTotalValue;

    @NotBlank(message = "Value should not be empty")
    private String orderPaymentMethod;

    private String orderDescription;

    private ZonedDateTime orderCreatedAt;

    @NotNull(message = "Value should not be empty")
    private Integer orderNumber;

    public UUID getOrderId() {
        return orderId;
    }

    public Float getOrderTotalValue() {
        return orderTotalValue;
    }

    public void setOrderTotalValue(Float orderTotalValue) {
        this.orderTotalValue = orderTotalValue;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public ZonedDateTime getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(ZonedDateTime orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order() {
        setOrderCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        setOrderId(UUID.randomUUID());
    }

    public Order(Float orderTotalValue, String orderPaymentMethod, int orderNumber) {
        this.orderTotalValue = orderTotalValue;
        this.orderPaymentMethod = orderPaymentMethod;
        this.orderNumber = orderNumber;
        setOrderCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        setOrderId(UUID.randomUUID());
    }

}
