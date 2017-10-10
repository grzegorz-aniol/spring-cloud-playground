package org.gangel.cloud.dataservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.cloud.dataservice.dto.ProductTO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@ActiveProfiles("pgsql")
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class IntegrationTestBase {

    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;
    protected final String CUSTOMER_ENDPOINT = "/api2/customers";
    protected final String PRODUCT_ENDPOINT = "/api2/products";
    protected final String ORDERS_ENDPOINT = "/api2/orders";
    protected final String CONTENTTYPE = MediaType.APPLICATION_JSON_VALUE;
    
    @Autowired
    protected WebApplicationContext wac;
    
    private Random rnd = new Random();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    protected String generateUnique() {
        return generateUnique(10);
    }

    protected String generateUnique(int limit) {
        return UUID.randomUUID().toString().substring(0, limit);
    }

    protected ProductTO generateProduct() {
        ProductTO c = new ProductTO();
        c.setDescription(generateUnique());
        c.setPrice(rnd.nextDouble()*1000);
        c.setTitle(generateUnique());
        return c;
    }

    protected CustomerTO generateCustomer() {
        CustomerTO c = new CustomerTO();
        c.setName(generateUnique());
        c.setLastname(generateUnique());
        c.setEmail(generateUnique(7)+"@"+generateUnique(5)+".com");
        return c;
    }

}
