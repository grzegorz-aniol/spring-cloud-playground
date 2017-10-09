package org.gangel.cloud.dataservice.service;

import org.gangel.cloud.dataservice.dto.ProductTO;
import org.gangel.cloud.dataservice.entity.Product;
import org.gangel.cloud.dataservice.repository.ProductRepository;
import org.gangel.cloud.dataservice.service.mappers.AbstractMapper;
import org.gangel.cloud.dataservice.service.mappers.ProductMapper;
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
