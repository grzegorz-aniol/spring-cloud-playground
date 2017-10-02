package org.gangel.cloud.dataservice.service;

import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.repository.CustomerRepository;
import org.gangel.cloud.dataservice.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersService {

    @Autowired
    protected CustomerRepository customerRepo;
    
    @Autowired
    protected OrdersRepository ordersRepo;

    @Autowired
    protected OrderItemMapper orderItemMapper;

    @Autowired
    protected OrderMapper orderMapper;
    
    @Transactional
    public void addNewOrder(OrdersTO orderTO) {
        orderTO.setId(null);
        Orders order = orderMapper.map(orderTO);
        ordersRepo.save(order);
    }
    
    @Transactional
    public OrdersTO getOrder(long orderId) {
        
        Orders orders = ordersRepo.findOne(orderId);
        if (orders == null) {
            return null;
        }
        
        return orderMapper.map(orders);        
    }
    
}
