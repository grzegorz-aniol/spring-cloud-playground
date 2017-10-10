package org.gangel.cloud.dataservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.gangel.cloud.dataservice.IntegrationTestBase;
import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.cloud.dataservice.dto.OrderItemTO;
import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.cloud.dataservice.dto.ProductTO;
import org.gangel.cloud.dataservice.repository.CustomerRepository;
import org.gangel.cloud.dataservice.repository.ProductRepository;
import org.gangel.cloud.dataservice.service.CustomerService;
import org.gangel.cloud.dataservice.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Random;

public class OrdersTest extends IntegrationTestBase {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;
    
    private ArrayList<Long> products, customers; 
    
    @Before
    public void generateData() {
        products = new ArrayList<>();
        customers = new ArrayList<>();
        
        for (int i=0; i < 1000; ++i) {
            ProductTO p = generateProduct();
            products.add(productService.save(p));
            CustomerTO c = generateCustomer();
            customers.add(customerService.save(c));
        }
    }
    
    @Test
    public void addNewOrders() throws Exception {

        Random rnd = new Random();
        
        for (int cnt=0; cnt < 1000; ++cnt) {
            Long customerId = customers.get(rnd.nextInt(customers.size()));
            
            OrdersTO orders = new OrdersTO();
            orders.setCustomerId(customerId);
            orders.setOrderItems(new ArrayList<OrderItemTO>());
            for (int i=0; i < 4; ++i) {
                OrderItemTO item = new OrderItemTO();
                item.setAmount(rnd.nextDouble()*1000);
                item.setQuantity(rnd.nextInt(9)+1);
                item.setPosition(i+1);
                item.setProductId(products.get(rnd.nextInt(products.size())));
                orders.getOrderItems().add(item);
            }
            
            MvcResult result = mockMvc.perform(post(ORDERS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(orders))
                    )
                    .andExpect(status().is(HttpStatus.CREATED.value()))
                    .andReturn();
        }
    }
    
}
