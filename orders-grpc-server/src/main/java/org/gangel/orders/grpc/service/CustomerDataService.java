package org.gangel.orders.grpc.service;

import org.gangel.orders.grpc.mappers.AbstractGrpcMapper;
import org.gangel.orders.proto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataService 
    extends AbstractDataService<org.gangel.orders.entity.Customer, Customer, Customer.Builder, Long> {

    @Autowired
    private PagingAndSortingRepository<org.gangel.orders.entity.Customer, Long> repository;
    
    @Autowired
    private AbstractGrpcMapper<org.gangel.orders.entity.Customer, Customer, Customer.Builder, Long> mapper; 
    
    @Override
    protected PagingAndSortingRepository<org.gangel.orders.entity.Customer, Long> getRepo() {
        return repository;
    }

    @Override
    protected AbstractGrpcMapper<org.gangel.orders.entity.Customer, Customer, Customer.Builder, Long> getMapper() {
        return mapper;
    }


}
