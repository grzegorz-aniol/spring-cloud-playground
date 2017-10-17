package org.gangel.cloud.dataservice.service.mappers;

import javax.annotation.Generated;
import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.orders.entity.Customer;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-10-17T14:55:14+0200",
    comments = "version: 1.2.0.CR2, compiler: javac, environment: Java 1.8.0_131 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl extends CustomerMapper {

    @Override
    public CustomerTO toDTO(Customer source) {
        if ( source == null ) {
            return null;
        }

        CustomerTO customerTO = new CustomerTO();

        if ( source.getId() != null ) {
            customerTO.setId( source.getId() );
        }
        if ( source.getName() != null ) {
            customerTO.setName( source.getName() );
        }
        if ( source.getLastname() != null ) {
            customerTO.setLastname( source.getLastname() );
        }
        if ( source.getPhone() != null ) {
            customerTO.setPhone( source.getPhone() );
        }
        if ( source.getEmail() != null ) {
            customerTO.setEmail( source.getEmail() );
        }

        return customerTO;
    }

    @Override
    public Customer toEntity(CustomerTO source) {
        if ( source == null ) {
            return null;
        }

        Customer customer = entityFactory( source, Customer.class );

        if ( source.getId() != null ) {
            customer.setId( source.getId() );
        }
        if ( source.getName() != null ) {
            customer.setName( source.getName() );
        }
        if ( source.getLastname() != null ) {
            customer.setLastname( source.getLastname() );
        }
        if ( source.getPhone() != null ) {
            customer.setPhone( source.getPhone() );
        }
        if ( source.getEmail() != null ) {
            customer.setEmail( source.getEmail() );
        }

        return customer;
    }
}
