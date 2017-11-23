package org.gangel.orders.httpservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.gangel.orders.dto.CustomerTO;
import org.gangel.orders.httpservice.ITIntegrationTestBase;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ITCustomerTest extends ITIntegrationTestBase {

    private static final long CUSTOMERS_COUNT = 5;

    @Test
    public void addNewCustomer() throws Exception {

        for (int i = 0; i < CUSTOMERS_COUNT; ++i) {

            CustomerTO c = generateCustomer();

            mockMvc.perform(post(CUSTOMER_ENDPOINT).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(c)))
                    .andExpect(status().is(HttpStatus.CREATED.value())).andReturn();
        }
    }

}
