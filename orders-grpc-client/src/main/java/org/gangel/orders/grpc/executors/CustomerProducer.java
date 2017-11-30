package org.gangel.orders.grpc.executors;

import org.gangel.orders.proto.Customer;

import java.util.UUID;

public class CustomerProducer {

    public static Customer produce() {
        return Customer.newBuilder()
                .setName(UUID.randomUUID().toString().substring(0, 10))
                .setLastname(UUID.randomUUID().toString().substring(0,10))
                .setEmail("test@domain.com")
                .build();        
    }
    
}
