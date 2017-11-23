package org.gangel.orders.grpc.mappers;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public CustomerMapper customerMapper() {
        return Mappers.getMapper(CustomerMapper.class);
    }
    
    @Bean
    public ProductMapper productMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }
    
    @Bean
    public OrdersMapper ordersMapper(){
        return Mappers.getMapper(OrdersMapper.class);
    }
    
    @Bean
    public OrderItemMapper orderItemMapper() {
        return Mappers.getMapper(OrderItemMapper.class);
    }
    
}
