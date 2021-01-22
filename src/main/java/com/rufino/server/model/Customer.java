package com.rufino.server.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "customers")
@JsonInclude(Include.NON_NULL)
public class Customer {

    @Id
    private UUID customerId;

    @NotBlank(message = "Value should not be empty")
    private String customerName;
    
    @NotBlank(message = "Value should not be empty")
    private String customerLastName;
    
    @NotBlank(message = "Value should not be empty")
    private String customerPhone;

    @NotBlank(message = "Value should not be empty")
    private String customerEmail;

    @NotNull(message = "Value should not be empty")
    @Column(columnDefinition = "timestamp with time zone")
    private ZonedDateTime customerCreatedAt;

    public Customer() {
        this.customerId = UUID.randomUUID();
        this.customerCreatedAt = ZonedDateTime.now(ZoneId.of("Z"));
    }

    public Customer(String id) {
        this.customerId = UUID.fromString(id);
        this.customerCreatedAt = ZonedDateTime.now(ZoneId.of("Z"));
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public ZonedDateTime getCustomerCreatedAt() {
        return customerCreatedAt;
    }

    public void setCustomerCreatedAt(ZonedDateTime customerCreatedAt) {
        this.customerCreatedAt = customerCreatedAt;
    }

	public UUID getCustomerId() {
		return customerId;
	}

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

}
