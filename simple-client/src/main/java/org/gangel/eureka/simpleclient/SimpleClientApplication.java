package org.gangel.eureka.simpleclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSecurity
@EnableHystrix
@RestController
public class SimpleClientApplication {

    @Autowired
    SimpleService simpleService;
    
	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}
	
	@RequestMapping("/calldirect")
	public String getMessage() {
	    return simpleService.callDirect();
	}
	
    @RequestMapping("/callwithribbon")
    public String getMessageWithRibbon() {
        return simpleService.callWithRibbon();
    }
	
}
