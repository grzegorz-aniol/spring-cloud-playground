package org.gangel.eureka.simpleclient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SimpleService {

    @Autowired
    private RestTemplateBuilder resetTemplBuilder;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private EurekaClient eurekaClient;    
    
    public String getDefaultValue() {
        return "unknown";
    }
    
    @HystrixCommand(commandKey="callDirect", groupKey="simpleservicecalls", fallbackMethod="getDefaultValue")
    public String callDirect() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("SimpleService", false);
        String baseUrl = instance.getHomePageUrl();
        RestTemplate template = resetTemplBuilder.build();
        ResponseEntity<String> response = template.getForEntity(baseUrl, String.class);
        return response.getBody();        
    }
    
    @HystrixCommand(commandKey="callWithRibbon", groupKey="simpleservicecalls", fallbackMethod="getDefaultValue")
    public String callWithRibbon() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://simpleservice/", String.class);
        return response.getBody();        
    }
}
