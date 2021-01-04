package com.rufino.server.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "orders")
@JsonInclude(Include.NON_NULL)
public class Order {

    @Id
    private UUID orderId;

    @NotNull(message = "Invalid customer id format")
    private UUID customerId;

    @NotNull(message = "Value should not be empty")
    private Float orderTotalValue;

    @NotBlank(message = "Value should not be empty")
    private String orderPaymentMethod;

    private String orderDescription;

    @NotNull(message = "Value should not be empty")
    private ZonedDateTime orderCreatedAt;

    @NotNull(message = "Value should not be empty")
    private Integer orderNumber;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        try {
            this.customerId = UUID.fromString(customerId);
        } catch (Exception e) {
            // e.printStackTrace();
        }
       
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order() {
        setOrderCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        setOrderId(UUID.randomUUID());
    }

}
