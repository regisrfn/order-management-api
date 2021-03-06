package com.rufino.server.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    @OneToOne()
    @JoinColumn(name = "customerId", referencedColumnName = "customerId")
    private Customer customer;

    @NotNull(message = "Value should not be empty")
    private Float orderTotalValue;

    @NotNull(message = "Value should not be empty")
    private PaymentCategory orderPaymentMethod;

    private String orderDescription;

    @NotNull(message = "Value should not be empty")
    @Column(columnDefinition = "timestamp with time zone")
    private ZonedDateTime orderCreatedAt;

    @NotNull(message = "Value should not be empty")
    private Integer orderNumber;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customer.getCustomerId();
    }

    public void setCustomerId(String customerId) {
        try {
            setCustomer(new Customer(customerId));
        } catch (IllegalArgumentException e) {
            this.customer= null;
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

    public PaymentCategory getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        try {
            this.orderPaymentMethod = PaymentCategory.valueOf(orderPaymentMethod.toUpperCase());
        } catch (Exception e) {
            this.orderPaymentMethod = null;
        }

    }

    public void setOrderPaymentMethod(PaymentCategory orderPaymentMethod) {
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order() {
        setOrderCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        setOrderId(UUID.randomUUID());
    }

    public enum PaymentCategory {
        CASH, CHECK, DEBIT_CARD, CREDIT_CARD, MOBILE, BANK_TRANSFER
    }

}
