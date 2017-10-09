package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.cloud.dataservice.entity.Customer;
import org.gangel.cloud.dataservice.service.AbstractService;
import org.gangel.cloud.dataservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/customers")
public class CustomerController extends AbstractController<Customer, CustomerTO, Long> {

    @Autowired
    protected CustomerService service;
    
    @Override
    protected AbstractService<Customer, CustomerTO, Long> getService() {
        return service;
    }
    
}
