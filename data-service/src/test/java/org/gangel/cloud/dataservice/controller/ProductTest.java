package org.gangel.cloud.dataservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.gangel.cloud.dataservice.IntegrationTestBase;
import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.cloud.dataservice.dto.ProductTO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

public class ProductTest extends IntegrationTestBase {

    @Test
    @Repeat(1000)
    public void addNewProduct() throws Exception {
        
        ProductTO c = generateProduct();
        
        MvcResult result = mockMvc.perform(post(PRODUCT_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(c))
             )
            .andExpect(status().is(HttpStatus.CREATED.value()))
            .andReturn();
    }
}
