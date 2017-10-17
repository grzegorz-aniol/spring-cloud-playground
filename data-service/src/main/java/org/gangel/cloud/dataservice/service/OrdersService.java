package org.gangel.cloud.dataservice.service;

import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.orders.entity.Orders;
import org.gangel.cloud.dataservice.repository.CustomerRepository;
import org.gangel.cloud.dataservice.repository.OrdersRepository;
import org.gangel.cloud.dataservice.service.mappers.AbstractMapper;
import org.gangel.cloud.dataservice.service.mappers.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class OrdersService extends AbstractService<Orders, OrdersTO, Long> {

    @Autowired
    protected CustomerRepository customerRepo;
    
    @Autowired
    protected OrdersRepository ordersRepo;

    @Autowired
    protected OrdersMapper mapper;
    
    @Override
    protected PagingAndSortingRepository<Orders, Long> getRepo() {
        return ordersRepo;
    }

    protected AbstractMapper<Orders, OrdersTO, Long> getMapper() {
        return mapper; 
    }
    
}
