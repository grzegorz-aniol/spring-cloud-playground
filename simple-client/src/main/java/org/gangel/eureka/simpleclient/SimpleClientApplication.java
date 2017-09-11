package org.gangel.eureka.simpleclient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class SimpleClientApplication {

    @Autowired
    private RestTemplateBuilder resetTemplBuilder;
    
    @Autowired
    private EurekaClient eurekaClient;
    
	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}
	
	@RequestMapping("/")
	public String getMessage() {
	    InstanceInfo instance = eurekaClient.getNextServerFromEureka("SimpleService", false);
	    String baseUrl = instance.getHomePageUrl();
	    RestTemplate restTemplate = resetTemplBuilder.build();
	    ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
	    return response.getBody();
	}
}
