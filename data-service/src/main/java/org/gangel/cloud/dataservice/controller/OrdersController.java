package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.repository.OrdersRepository;
import org.gangel.cloud.dataservice.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    
    @RequestMapping(method=RequestMethod.GET, path="/{id}")
    @ResponseStatus(value=HttpStatus.OK)
    public OrdersTO getOrder(@PathVariable("id") Long id) {
        return ordersService.getOrder(id); 
    }
    
    @RequestMapping(method=RequestMethod.POST) 
    @ResponseStatus(value=HttpStatus.ACCEPTED)
    public void addNewOrder(@RequestBody OrdersTO ordersTO) {
        ordersService.addNewOrder(ordersTO);
    }
    
}
