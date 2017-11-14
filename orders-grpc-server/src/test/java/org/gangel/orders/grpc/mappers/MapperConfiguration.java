package org.gangel.orders.grpc.mappers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public CustomerMapper customerMapper() {
        return CustomerMapper.INSTANCE;
    }
    
    @Bean
    public CustomerMapper.BuilderFactory customerMapperBuilderFactory() {
        return new CustomerMapper.BuilderFactory();
    }
    
    @Bean
    public ProductMapper productMapper() {
        return ProductMapper.INSTANCE;
    }
    
    @Bean
    public ProductMapper.BuilderFactory productMapperBuilderFactory() {
        return new ProductMapper.BuilderFactory();
    }
    
    @Bean
    public OrdersMapper ordersMapper(){
        return OrdersMapper.INSTANCE;
    }
    
    @Bean
    public OrdersMapper.BuilderFactory ordersMapperBuilderFactory() {
        return new OrdersMapper.BuilderFactory();
    }
    
    @Bean
    public OrderItemMapper orderItemMapper() {
        return OrderItemMapper.INSTANCE;
    }
    
    @Bean
    public OrderItemMapper.BuilderFactory orderItemMapperBuilderFactory() {
        return new OrderItemMapper.BuilderFactory();
    }
}
