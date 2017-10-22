package org.gangel.cloud.dataservice.controller;

import org.gangel.common.services.AbstractService;
import org.gangel.orders.dto.CustomerTO;
import org.gangel.orders.entity.Customer;
import org.gangel.orders.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/customers")
public class CustomerController extends AbstractController<Customer, CustomerTO, Long> {

    public final static String ENDPOINT = "/api2/customers"; 
    
    @Autowired
    protected CustomerService service;
    
    @Override
    protected AbstractService<Customer, CustomerTO, Long> getService() {
        return service;
    }

    @Override
    protected String getEndpointRoot() {
        return ENDPOINT;
    }
    
}
