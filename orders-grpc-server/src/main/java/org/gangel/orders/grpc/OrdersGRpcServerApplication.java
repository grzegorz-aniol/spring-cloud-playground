package org.gangel.orders.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrdersGRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersGRpcServerApplication.class, args);
    }    
    
}
