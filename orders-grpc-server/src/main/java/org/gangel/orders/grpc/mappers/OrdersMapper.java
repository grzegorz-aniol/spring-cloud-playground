package org.gangel.orders.grpc.mappers;

import lombok.val;
import org.gangel.orders.grpc.mappers.OrdersMapper.BuilderFactory;
import org.gangel.orders.proto.Orders;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses={ BuilderFactory.class, CustomerMapper.class, OrderItemMapper.class }, 
        config=MappingConfiguration.class)
public abstract class OrdersMapper {

    public static OrdersMapper INSTANCE = Mappers.getMapper(OrdersMapper.class);
    
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Mappings({
        @Mapping(target="customer.id", source="customerId"),
        @Mapping(target="orderItems", source="orderItemList")
    })
    public abstract org.gangel.orders.entity.Orders map(Orders orders);
    
    @Mappings({
        @Mapping(target="customerId", source="customer.id"),
    })    
    public abstract Orders.Builder map( org.gangel.orders.entity.Orders ordersEntity );
    
    @AfterMapping
    public void mapOrderItems(@MappingTarget Orders.Builder builder, org.gangel.orders.entity.Orders ordersEntity ) {
        if (ordersEntity != null && builder != null && ordersEntity.getOrderItems()!=null) {
            ordersEntity.getOrderItems().stream()
            .forEach((i) -> {
                val v = orderItemMapper.map(i);
                builder.addOrderItem(v != null ? v.build() : null); 
            });   
        }
    }

    public static class  BuilderFactory {
        Orders.Builder builder() {
            return Orders.newBuilder();
        }
    }
}
