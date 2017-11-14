package org.gangel.orders.grpc.mappers;

import lombok.val;
import org.gangel.orders.grpc.mappers.OrderItemMapper.BuilderFactory;
import org.gangel.orders.proto.OrderItem;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Mapper(uses={ BuilderFactory.class, CustomerMapper.class }, 
        unmappedTargetPolicy = ReportingPolicy.WARN,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class OrderItemMapper {

    public static OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    
    @Mappings({
        @Mapping(target="product.id", source="productId")
    })
    public abstract org.gangel.orders.entity.OrderItem map(OrderItem orderItem);
    
    @InheritInverseConfiguration
    public abstract OrderItem.Builder map( org.gangel.orders.entity.OrderItem ordersEntity );
    
    public Collection<org.gangel.orders.entity.OrderItem> map(Collection<OrderItem> source) {
        if (source == null) {
            return null;
        }
        return source.stream().map((i) -> map(i)).collect(Collectors.toCollection(TreeSet::new));
    }
    
    public List<OrderItem> mapEntities(Collection<org.gangel.orders.entity.OrderItem> source) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map((i) -> {
                    val v = map(i);
                    if (v==null) return null;
                    return v.build(); 
                })
                .collect(Collectors.toList());
    }    
  
    public static class  BuilderFactory {
        OrderItem.Builder builder() {
            return OrderItem.newBuilder();
        }
    }
}
