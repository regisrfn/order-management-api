package com.rufino.server.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.data.domain.Page;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = { "page" })
public class PageResponse {

    private List<Order> ordersList;
    private Integer totalPages, pageNumber;
    private Page<Order> page;

    public PageResponse() {
    }

    public PageResponse(Page<Order> page) {
        this.page = page;
        this.ordersList = page.toList();
        this.pageNumber = page.getNumber();
        this.totalPages = page.getTotalPages();
    }

    public List<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Page<Order> Page() {
        return page;
    }

    public void setPage(Page<Order> page) {
        this.page = page;
    }

}
