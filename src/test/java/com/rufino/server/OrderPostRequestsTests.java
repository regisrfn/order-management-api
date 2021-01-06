package com.rufino.server;

import org.json.JSONException;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.core.Is;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderPostRequestsTests {

        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Autowired
        private MockMvc mockMvc;

        @BeforeEach
        void clearTable() {
                jdbcTemplate.update("DELETE FROM ORDERS");
        }

        @Test
        void itShouldSaveOrder() throws Exception {
                JSONObject my_obj = new JSONObject();

                my_obj.put("customerId", "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
                my_obj.put("orderTotalValue", 1.99f);
                my_obj.put("orderPaymentMethod", "card");
                my_obj.put("orderNumber", 123123);
                mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalValue", Is.is(1.99)))
                                .andExpect(status().isOk()).andReturn();

        }

        @Test
        void itShouldSaveOrder_withId() throws Exception {
                JSONObject my_obj = new JSONObject();

                my_obj.put("customerId", "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
                my_obj.put("orderTotalValue", 1.99f);
                my_obj.put("orderPaymentMethod", "card");
                my_obj.put("orderNumber", 123123);
                my_obj.put("orderId", "40d067ae-0fbd-4bb8-b306-65ac40737aaa");

                
                mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalValue", Is.is(1.99)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId", Is.is("40d067ae-0fbd-4bb8-b306-65ac40737aaa")))
                                .andExpect(status().isOk()).andReturn();

        }

        @Test
        void itShouldNotSaveOrder() throws Exception {
                JSONObject my_obj = new JSONObject();
                my_obj.put("customerId", "cba3ff2e-3087-49bd-bc9b-285e809e7b32");
                my_obj.put("orderTotalValue", 1.99f);
                my_obj.put("orderNumber", 123123);
                mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.orderPaymentMethod",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

                my_obj.put("orderPaymentMethod", "  ");
                mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.orderPaymentMethod",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

                my_obj.put("orderPaymentMethod", null);
                mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.orderPaymentMethod",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

                ///////////////////////////////////////////////////////////////////////////////
                my_obj = new JSONObject();
                mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.orderPaymentMethod",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.orderTotalValue",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.orderNumber",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.customerId",
                                                Is.is("Invalid customer id format")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

        }

        @Test
        void itShouldNotSaveOrder_invalidCustomerId() {
                JSONObject my_obj = new JSONObject();
                try {
                        my_obj.put("customerId", "cba3ff2e");
                        my_obj.put("orderPaymentMethod", "card");
                        my_obj.put("orderTotalValue", 1.99f);
                        my_obj.put("orderNumber", 123123);
                        mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON)
                                        .content(my_obj.toString()))
                                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.customerId",
                                                        Is.is("Invalid customer id format")))
                                        .andExpect(status().isBadRequest()).andReturn();
                } catch (JSONException e) {
                        assert (false);
                        e.printStackTrace();
                } catch (Exception e) {
                        assert (false);
                        e.printStackTrace();
                }

        }

}
