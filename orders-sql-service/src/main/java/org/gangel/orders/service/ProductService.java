package org.gangel.orders.service;

import org.gangel.common.services.AbstractMapper;
import org.gangel.common.services.AbstractService;
import org.gangel.orders.dto.ProductTO;
import org.gangel.orders.entity.Product;
import org.gangel.orders.repository.ProductRepository;
import org.gangel.orders.service.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractService<Product, ProductTO, Long> {

    @Autowired
    protected ProductRepository repo;
    
    @Autowired
    protected ProductMapper mapper;
    
    @Override
    protected PagingAndSortingRepository<Product, Long> getRepo() {
        return repo;
    }

    @Override
    protected AbstractMapper<Product, ProductTO, Long> getMapper() {
        return mapper;
    }

}
