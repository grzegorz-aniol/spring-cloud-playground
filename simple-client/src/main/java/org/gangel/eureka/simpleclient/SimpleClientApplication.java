package org.gangel.eureka.simpleclient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSecurity
@RestController
public class SimpleClientApplication {

    @Autowired
    private RestTemplateBuilder resetTemplBuilder;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private EurekaClient eurekaClient;
    
	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}
	
	@RequestMapping("/calldirect")
	public String getMessage() {
	    InstanceInfo instance = eurekaClient.getNextServerFromEureka("SimpleService", false);
	    String baseUrl = instance.getHomePageUrl();
	    RestTemplate template = resetTemplBuilder.build();
	    ResponseEntity<String> response = template.getForEntity(baseUrl, String.class);
	    return response.getBody();
	}
	
    @RequestMapping("/callwithribbon")
    public String getMessageWithRibbon() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://simpleservice/", String.class);
        return response.getBody();
    }
	
}
