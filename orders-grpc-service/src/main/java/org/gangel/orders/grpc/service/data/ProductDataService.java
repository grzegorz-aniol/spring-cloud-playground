package org.gangel.orders.grpc.service.data;

import org.gangel.orders.grpc.mappers.AbstractGrpcMapper;
import org.gangel.orders.grpc.mappers.ProductMapper;
import org.gangel.orders.proto.Product;
import org.gangel.orders.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductDataService 
    extends AbstractDataService<org.gangel.orders.entity.Product, Product, Product.Builder, Long> {

    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private ProductMapper mapper; 
    
    @Override
    protected PagingAndSortingRepository<org.gangel.orders.entity.Product, Long> getRepo() {
        return repository;
    }

    @Override
    protected AbstractGrpcMapper<org.gangel.orders.entity.Product, Product, Product.Builder, Long> getMapper() {
        return mapper;
    }

}
