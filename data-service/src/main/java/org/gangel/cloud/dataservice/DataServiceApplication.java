package org.gangel.cloud.dataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages={"org.gangel.orders", "org.gangel.cloud.dataservice"})
@EnableJpaRepositories(basePackages="org.gangel.orders.repository")
@EntityScan(basePackages="org.gangel.orders.entity")
@EnableDiscoveryClient
@EnableScheduling
public class DataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataServiceApplication.class, args);
	}
	
}
