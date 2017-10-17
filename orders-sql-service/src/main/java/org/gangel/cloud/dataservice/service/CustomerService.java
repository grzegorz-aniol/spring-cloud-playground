package org.gangel.cloud.dataservice.service;

import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.cloud.dataservice.dto.ProductTO;
import org.gangel.orders.entity.Customer;
import org.gangel.cloud.dataservice.repository.CustomerRepository;
import org.gangel.cloud.dataservice.service.mappers.AbstractMapper;
import org.gangel.cloud.dataservice.service.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends AbstractService<Customer, CustomerTO, Long> {

    @Autowired
    protected CustomerRepository repo;
    
    @Autowired
    protected CustomerMapper mapper;
    
    @Override
    protected PagingAndSortingRepository<Customer, Long> getRepo() {
        return repo;
    }

    @Override
    protected AbstractMapper<Customer, CustomerTO, Long> getMapper() {
        return mapper;
    }

}
