package org.gangel.orders.grpc.mappers;

import org.gangel.orders.proto.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(config = MappingConfiguration.class)
public abstract class ProductMapper extends
        AbstractGrpcMapper<org.gangel.orders.entity.Product, Product, Product.Builder, Long> {

    @ObjectFactory
    public Product.Builder getBuilder() {
        return Product.newBuilder();
    }

    @Override
    public Long getIdentifier(Product dto) {
        return dto.getId();
    }
}
