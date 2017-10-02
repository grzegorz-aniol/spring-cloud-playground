package org.gangel.cloud.dataservice.service;

import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CustomerRepository customerRepo;
    
    public OrdersTO map(Orders orders) {
        return OrdersTO.builder()
            .id(orders.getId())
            .customerId(orders.getCustomer().getId())
            .orderItems(orderItemMapper.map(orders.getOrderItems()))
            .build();
    }

    public Orders map(OrdersTO o) {
        Orders result = new Orders();
        result.setId(o.getId());
        result.setCustomer(customerRepo.findOne(o.getCustomerId()));
        result.setOrderItems(orderItemMapper.map(result, o.getOrderItems()));
        return result;
    }
}
