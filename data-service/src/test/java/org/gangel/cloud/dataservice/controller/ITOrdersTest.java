package org.gangel.cloud.dataservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.gangel.cloud.dataservice.ITIntegrationTestBase;
import org.gangel.orders.dto.CustomerTO;
import org.gangel.orders.dto.OrderItemTO;
import org.gangel.orders.dto.OrdersTO;
import org.gangel.orders.dto.ProductTO;
import org.gangel.orders.service.CustomerService;
import org.gangel.orders.service.ProductService;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Slf4j
public class ITOrdersTest extends ITIntegrationTestBase {

    private static final long ORDERS_COUNT = 1000;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    private ArrayList<Long> products, customers; 
    
    @Before
    public void generateData() {
        products = new ArrayList<>();
        customers = new ArrayList<>();
        
        inTransaction(() -> {
            for (int i=0; i < ORDERS_COUNT; ++i) {
                ProductTO p = generateProduct();
                products.add(productService.save(p));
                CustomerTO c = generateCustomer();
                customers.add(customerService.save(c));
            }            
        });
    }
    
    @Test
    public void addNewOrders() throws Exception {

        Random rnd = new Random();
        
        long startTime = System.currentTimeMillis();
        
        for (int cnt=0; cnt < ORDERS_COUNT; ++cnt) {
            Long customerId = customers.get(rnd.nextInt(customers.size()));
            
            inTransaction(() -> {
                OrdersTO orders = new OrdersTO();
                orders.setCustomerId(customerId);
                orders.setOrderItems(new ArrayList<OrderItemTO>());
                for (int i=0; i < 4; ++i) {
                    OrderItemTO item = new OrderItemTO();
                    item.setAmount(rnd.nextDouble()*ORDERS_COUNT);
                    item.setQuantity(rnd.nextInt(9)+1);
                    item.setPosition(i+1);
                    item.setProductId(products.get(rnd.nextInt(products.size())));
                    orders.getOrderItems().add(item);
                }
                
                try {
                    mockMvc.perform(post(ORDERS_ENDPOINT)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(orders))
                            )
                    .andExpect(status().is(HttpStatus.CREATED.value()))
                    .andReturn();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
            });
        }
        
        long duration = System.currentTimeMillis() - startTime; 
        
        log.info(String.format("Total time = %.3f; IOPS = %.3f; average latency = %.2f ms", 
                1e-3*duration, ORDERS_COUNT/(1e-3*duration), (double)duration/ORDERS_COUNT));
    }

    @After
    public void showStats() {
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
        String json;
        try {
            json = mapper.writeValueAsString(statistics);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return;
        }
        log.info(json);
    }
}
