package org.gangel.orders.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="org.gangel.orders.repository")
@EntityScan(basePackages="org.gangel.orders.entity")
public class OrdersGRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersGRpcServerApplication.class, args);
    }    
    
}
