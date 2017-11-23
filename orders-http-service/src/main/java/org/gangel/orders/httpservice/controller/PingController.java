package org.gangel.orders.httpservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @RequestMapping(path="/ping", method=RequestMethod.GET)
    public String ping() {
        return "{ \"message\" : \"Hello!\" }";
    }
    
    @RequestMapping(path="/", method=RequestMethod.GET)
    public String hello() {
        return "{ \"message\" : \"Hello!\" }";
    }
    
}
