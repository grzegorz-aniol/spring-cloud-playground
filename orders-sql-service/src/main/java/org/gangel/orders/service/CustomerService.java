package org.gangel.orders.service;

import org.gangel.common.services.AbstractMapper;
import org.gangel.common.services.AbstractService;
import org.gangel.orders.dto.CustomerTO;
import org.gangel.orders.entity.Customer;
import org.gangel.orders.repository.CustomerRepository;
import org.gangel.orders.repository.IdsRange;
import org.gangel.orders.service.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly=true) 
    public IdsRange getIdsRange() {
        return repo.getIdsRange();
    }
}
