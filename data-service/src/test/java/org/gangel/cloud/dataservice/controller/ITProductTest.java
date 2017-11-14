package org.gangel.cloud.dataservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.gangel.cloud.dataservice.ITIntegrationTestBase;
import org.gangel.orders.dto.ProductTO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ITProductTest extends ITIntegrationTestBase {

    private final static int PRODUCTS_COUNT = 5;

    @Test
    public void addNewProduct() throws Exception {

        for (int i = 0; i < PRODUCTS_COUNT; ++i) {

            ProductTO c = generateProduct();

            mockMvc.perform(post(PRODUCT_ENDPOINT).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(c)))
                    .andExpect(status().is(HttpStatus.CREATED.value())).andReturn();
        }
    }
}
