package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.repository.OrdersRepository;
import org.gangel.cloud.dataservice.service.AbstractService;
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
@RequestMapping(OrdersController.ENDPOINT)
public class OrdersController extends AbstractController<Orders, OrdersTO, Long> {

    public final static String ENDPOINT = "/api2/orders"; 
    
    @Autowired
    private OrdersService ordersService;
    
    @Override
    protected AbstractService<Orders, OrdersTO, Long> getService() {
        return ordersService;
    }

    @Override
    protected String getEndpointRoot() {
        return ENDPOINT;
    }
    
}
