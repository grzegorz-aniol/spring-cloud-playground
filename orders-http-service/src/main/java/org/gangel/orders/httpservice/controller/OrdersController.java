package org.gangel.orders.httpservice.controller;

import org.gangel.common.services.AbstractController;
import org.gangel.common.services.AbstractService;
import org.gangel.orders.dto.OrdersTO;
import org.gangel.orders.entity.Orders;
import org.gangel.orders.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
