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

public class CustomerTest extends IntegrationTestBase {

    @Test
    @Repeat(1000)
    public void addNewCustomer() throws Exception {
        
        CustomerTO c = generateCustomer();
        
        MvcResult result = mockMvc.perform(post(CUSTOMER_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(c))
             )
            .andExpect(status().is(HttpStatus.CREATED.value()))
            .andReturn();
    }
    
}
