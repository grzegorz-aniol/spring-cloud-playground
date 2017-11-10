package org.gangel.orders.service;

import org.gangel.common.services.AbstractMapper;
import org.gangel.common.services.AbstractService;
import org.gangel.orders.dto.OrdersTO;
import org.gangel.orders.entity.Orders;
import org.gangel.orders.repository.CustomerRepository;
import org.gangel.orders.repository.OrdersRepository;
import org.gangel.orders.service.mappers.OrdersMapper;
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

//    @Override
//    public void beforeSave(@NonNull Orders entity) {
//        //entity.getOrderItems().stream().mapToDouble(e -> e.getAmount() * e.getQuantity()).sum();
//    }
//
//    @Override
//    public void beforeUpdate(@NonNull Orders entity) {
//    }
    
}
