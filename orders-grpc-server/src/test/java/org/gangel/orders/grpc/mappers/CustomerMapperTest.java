package org.gangel.orders.grpc.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import lombok.val;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.Customer.Builder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerMapperTest {

    @InjectMocks
    private CustomerMapper customerMapper = CustomerMapper.INSTANCE;
    
    @Mock
    CustomerMapper.BuilderFactory builderFactory;
    
    @Before
    public void onSetUp() {
        when(builderFactory.builder()).thenReturn(Customer.newBuilder());
    }
    
    @Test
    public void convertProtoToEntity() {
        assertNotNull(customerMapper);
        
        final String VALUE_NAME = "name";
        final String VALUE_LASTNAME = "lastname";
        final String VALUE_EMAIL = "email@domain.com";
        
        Customer customerProto = Customer.newBuilder()
                .setId(1)
                .setName(VALUE_NAME)
                .setLastname(VALUE_LASTNAME)
                .setEmail(VALUE_EMAIL)
                .build();
        
        
        org.gangel.orders.entity.Customer customerEntity = customerMapper.map(customerProto);
        
        assertEquals(new Long(1L), customerEntity.getId());
        assertEquals(VALUE_NAME, customerEntity.getName());
        assertEquals(VALUE_LASTNAME, customerEntity.getLastname());
        assertEquals(VALUE_EMAIL, customerEntity.getEmail());
    }
    
    @Test
    public void convertEntityToProto() {
        final String VALUE_NAME = "name";
        final String VALUE_LASTNAME = "lastname";
        final String VALUE_EMAIL = "email@domain.com";
        
        val customerEntity = new org.gangel.orders.entity.Customer(); 
        customerEntity.setId(1L);
        customerEntity.setName(VALUE_NAME);
        customerEntity.setLastname(VALUE_LASTNAME);
        customerEntity.setEmail(VALUE_EMAIL);
        
        Builder builder = customerMapper.map(customerEntity);
        
        assertNotNull(builder);
        Customer customer = builder.build();
        
        assertEquals(1L, customer.getId());
        assertEquals(VALUE_NAME, customer.getName());
        assertEquals(VALUE_LASTNAME, customer.getLastname());
        assertEquals(VALUE_EMAIL, customer.getEmail());
    }    
}
