package org.gangel.cloud.dataservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.gangel.cloud.dataservice.IntegrationTestBase;
import org.gangel.orders.dto.CustomerTO;
import org.gangel.orders.dto.OrderItemTO;
import org.gangel.orders.dto.OrdersTO;
import org.gangel.orders.dto.ProductTO;
import org.gangel.orders.service.CustomerService;
import org.gangel.orders.service.ProductService;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Slf4j
public class OrdersTest extends IntegrationTestBase {

    private static final long ORDERS_COUNT = 5;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private ArrayList<Long> products, customers; 
    
    @Before
    public void generateData() {
        products = new ArrayList<>();
        customers = new ArrayList<>();
        
        for (int i=0; i < ORDERS_COUNT; ++i) {
            ProductTO p = generateProduct();
            products.add(productService.save(p));
            CustomerTO c = generateCustomer();
            customers.add(customerService.save(c));
        }
    }
    
    @Test
    
    public void addNewOrders() throws Exception {

        Random rnd = new Random();
        
        for (int cnt=0; cnt < ORDERS_COUNT; ++cnt) {
            Long customerId = customers.get(rnd.nextInt(customers.size()));
            
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
            
            mockMvc.perform(post(ORDERS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(orders))
                    )
                    .andExpect(status().is(HttpStatus.CREATED.value()))
                    .andReturn();
        }
    }

    @After
    public void showStats() {
        Statistics statistics = entityManager.unwrap(Session.class).getSessionFactory().getStatistics();
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
