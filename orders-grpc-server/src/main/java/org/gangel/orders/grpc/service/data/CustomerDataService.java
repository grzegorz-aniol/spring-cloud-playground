package org.gangel.orders.grpc.service.data;

import org.gangel.orders.grpc.mappers.AbstractGrpcMapper;
import org.gangel.orders.grpc.mappers.CustomerMapper;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataService 
    extends AbstractDataService<org.gangel.orders.entity.Customer, Customer, Customer.Builder, Long> {

    @Autowired
    private CustomerRepository repository;
    
    @Autowired
    private CustomerMapper mapper; 
    
    @Override
    protected PagingAndSortingRepository<org.gangel.orders.entity.Customer, Long> getRepo() {
        return repository;
    }

    @Override
    protected AbstractGrpcMapper<org.gangel.orders.entity.Customer, Customer, Customer.Builder, Long> getMapper() {
        return mapper;
    }

}
