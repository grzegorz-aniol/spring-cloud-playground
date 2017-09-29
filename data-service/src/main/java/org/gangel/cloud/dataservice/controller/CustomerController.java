package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.entity.Customer;
import org.gangel.cloud.dataservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepo;
    
    @RequestMapping(method=RequestMethod.GET, path="/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return customerRepo.findOne(id);
    }
    
    @RequestMapping(method=RequestMethod.POST, path="/") 
    public void addNewCustromer(Customer customer) {
        customerRepo.save(customer);
    }
    
}
