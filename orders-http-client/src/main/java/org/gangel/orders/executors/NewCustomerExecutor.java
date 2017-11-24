package org.gangel.orders.executors;

import lombok.SneakyThrows;
import org.apache.http.client.methods.HttpUriRequest;
import org.gangel.orders.dto.CustomerTO;

import java.util.UUID;

public class NewCustomerExecutor extends AbstractTaskExecutor {

    public NewCustomerExecutor(){
        
    }
    
    @Override
    @SneakyThrows
    public HttpUriRequest requestSupplier() {
        CustomerTO customer = new CustomerTO();
        customer.setName(UUID.randomUUID().toString().substring(0, 10));
        customer.setLastname(UUID.randomUUID().toString().substring(0,10));
        customer.setEmail("test@domain.com");
        String value = mapper.writeValueAsString(customer);
        return requestPostBuilder("/api2/customers", value);
    }

}
