package org.gangel.orders.grpc.mappers;

import lombok.val;
import org.gangel.orders.grpc.mappers.CustomerMapper.BuilderFactory;
import org.gangel.orders.proto.Customer;
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
public abstract class CustomerMapper {

    public static CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    public abstract Customer.Builder map( org.gangel.orders.entity.Customer customerEntity );
    
    public abstract org.gangel.orders.entity.Customer map(Customer customer);
    
    @ObjectFactory
    public org.gangel.orders.entity.Customer customerFactory(Customer customer) {
        val c = new org.gangel.orders.entity.Customer();
        if (customer == null) {
            return c;
        }
        c.setId(customer.getId());
        return c;
    }

    public static class BuilderFactory {
        Customer.Builder builder() {
            return Customer.newBuilder();
        }
    }
}
