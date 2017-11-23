package org.gangel.orders.grpc.mappers;

import org.gangel.orders.proto.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(config=MappingConfiguration.class)
public abstract class CustomerMapper extends AbstractGrpcMapper<org.gangel.orders.entity.Customer, Customer, Customer.Builder, Long> {

    @Override
    public Long getIdentifier(Customer dto) {
        return dto.getId();
    }
    
    @ObjectFactory
    public Customer.Builder getBuilder() {
        return Customer.newBuilder();        
    }
    
}
