package org.gangel.orders.grpc.service.data;

import org.gangel.orders.grpc.mappers.AbstractGrpcMapper;
import org.gangel.orders.grpc.mappers.OrdersMapper;
import org.gangel.orders.proto.Orders;
import org.gangel.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Component
public class OrdersDataService 
    extends AbstractDataService<org.gangel.orders.entity.Orders, Orders, Orders.Builder, Long> {

    @Autowired
    private OrdersRepository repository;
    
    @Autowired
    private OrdersMapper mapper; 
    
    @Override
    protected PagingAndSortingRepository<org.gangel.orders.entity.Orders, Long> getRepo() {
        return repository;
    }

    @Override
    protected AbstractGrpcMapper<org.gangel.orders.entity.Orders, Orders, Orders.Builder, Long> getMapper() {
        return mapper;
    }
    
    @Transactional(readOnly=true) 
    public Stream<Orders> getCustomerOrders(long customerId) {
       return repository.findByCustomerId(customerId).map(e -> mapper.toProto(e).build());
    }
     
}
