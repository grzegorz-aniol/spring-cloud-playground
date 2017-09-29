package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/orders")
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepo;
    
    @RequestMapping(method=RequestMethod.GET, path="/{id}")
    public Orders getOrder(@PathVariable("id") Long id) {
        return ordersRepo.findOne(id);
    }
    
    @RequestMapping(method=RequestMethod.POST, path="/") 
    public void addNewOrder(@RequestBody Orders orders) {
        ordersRepo.save(orders);
    }
    
}
