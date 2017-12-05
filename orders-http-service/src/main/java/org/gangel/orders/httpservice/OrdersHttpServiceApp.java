package org.gangel.orders.httpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"org.gangel.orders", "org.gangel.orders.httpservice"})
@EnableJpaRepositories(basePackages="org.gangel.orders.repository")
@EntityScan(basePackages="org.gangel.orders.entity")
public class OrdersHttpServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(OrdersHttpServiceApp.class, args);
	}
	
}
