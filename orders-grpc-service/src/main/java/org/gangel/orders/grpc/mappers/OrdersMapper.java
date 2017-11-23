package org.gangel.orders.grpc.mappers;

import lombok.val;
import org.gangel.orders.proto.Orders;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses={ CustomerMapper.class, OrderItemMapper.class }, 
        config=MappingConfiguration.class)
public abstract class OrdersMapper extends AbstractGrpcMapper<org.gangel.orders.entity.Orders, Orders, Orders.Builder, Long>{

    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CustomerMapper customerMapper;

    @Mappings({
        @Mapping(target="customer.id", source="customerId"),
        @Mapping(target="orderItems", source="orderItemList")
    })
    @Override
    public abstract org.gangel.orders.entity.Orders toEntity(Orders orders);
    
    @Mappings({
        @Mapping(target="customerId", ignore=true),
    })    
    @Override
    public abstract Orders.Builder toProto( org.gangel.orders.entity.Orders ordersEntity );
    
    @AfterMapping
    public void mapOrderItems(@MappingTarget Orders.Builder builder, org.gangel.orders.entity.Orders ordersEntity ) {
        if (ordersEntity != null && builder != null && ordersEntity.getOrderItems()!=null) {
            ordersEntity.getOrderItems().stream()
            .forEach((i) -> {
                val v = orderItemMapper.toProto(i);
                builder.addOrderItem(v != null ? v.build() : null); 
            });   
        }
    }
    
    @AfterMapping
    public void mapOrderItems(@MappingTarget org.gangel.orders.entity.Orders ordersEntity, Orders orders) {
        if (orders!=null && ordersEntity!=null) {
            ordersEntity.setCustomer(customerMapper.fetchObject(orders.getCustomerId(), org.gangel.orders.entity.Customer.class));
            ordersEntity.getOrderItems().stream().forEach(i -> i.setOrder(ordersEntity));
        }
    }

    @Override
    public Long getIdentifier(Orders dto) {
        return dto.getId();
    }
        
    @ObjectFactory
    public Orders.Builder getBuilder() {
        return Orders.newBuilder();
    }
}
