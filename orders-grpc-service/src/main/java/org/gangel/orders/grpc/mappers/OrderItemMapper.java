package org.gangel.orders.grpc.mappers;

import lombok.val;
import org.gangel.orders.proto.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Mapper(uses={ CustomerMapper.class }, 
        config=MappingConfiguration.class   )
public abstract class OrderItemMapper extends AbstractGrpcMapper<org.gangel.orders.entity.OrderItem, OrderItem, OrderItem.Builder, Long>{

    @Autowired
    private ProductMapper productMapper;
    
    @Mappings({
        @Mapping(target="product.id", source="productId")
    })
    @Override
    public abstract org.gangel.orders.entity.OrderItem toEntity(OrderItem orderItem);
    
    @InheritInverseConfiguration
    @Override
    public abstract OrderItem.Builder toProto( org.gangel.orders.entity.OrderItem ordersEntity );
    
    public Collection<org.gangel.orders.entity.OrderItem> map(Collection<OrderItem> source) {
        if (source == null) {
            return null;
        }
        return source.stream().map((i) -> toEntity(i)).collect(Collectors.toCollection(TreeSet::new));
    }
    
    public List<OrderItem> mapEntities(Collection<org.gangel.orders.entity.OrderItem> source) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map((i) -> {
                    val v = toProto(i);                    
                    if (v==null) return null;
                    return v.build(); 
                })
                .collect(Collectors.toList());
    }    
    
    @AfterMapping
    public void fetchProduct(@MappingTarget org.gangel.orders.entity.OrderItem orderItemEntity, OrderItem orderItem) {
        orderItemEntity.setProduct(productMapper.fetchObject(orderItem.getProductId(), org.gangel.orders.entity.Product.class));
    }
  
    @Override
    public Long getIdentifier(OrderItem dto) {
        return dto.getId();
    }
        
    @ObjectFactory
    public OrderItem.Builder getBuilder() {
        return OrderItem.newBuilder();
    }
}
