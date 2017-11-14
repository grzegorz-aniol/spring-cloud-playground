package org.gangel.orders.grpc.mappers;

import lombok.val;
import org.gangel.orders.grpc.mappers.ProductMapper.BuilderFactory;
import org.gangel.orders.proto.Product;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses=BuilderFactory.class, 
        unmappedTargetPolicy = ReportingPolicy.WARN,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ProductMapper {

    public static ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    public abstract Product.Builder map( org.gangel.orders.entity.Product productEntity );
    
    public abstract org.gangel.orders.entity.Product map(Product product);
    
    @ObjectFactory
    public org.gangel.orders.entity.Product objectFactory(Product product) {
        if (product == null) {
            return new org.gangel.orders.entity.Product();
        }
        val entity = new org.gangel.orders.entity.Product();
        return entity;
    }

    public static class  BuilderFactory {
        Product.Builder builder() {
            return Product.newBuilder();
        }
    }
}
