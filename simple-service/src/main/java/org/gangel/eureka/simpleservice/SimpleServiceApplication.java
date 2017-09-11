package org.gangel.eureka.simpleservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class SimpleServiceApplication {

    @Value("${server.port}")
    private int instancePort;
    
	public static void main(String[] args) {
		SpringApplication.run(SimpleServiceApplication.class, args);
	}
	
	@RequestMapping("/")
	public String message() {
	    return "Instance="+String.valueOf(instancePort);
	}
	
}
