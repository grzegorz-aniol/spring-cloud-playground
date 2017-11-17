package org.gangel.orders.grpc.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import lombok.val;
import org.gangel.orders.proto.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class CustomerMapperTest {

    private static final long OBJECT_ID = 123L;

    @InjectMocks
    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Mock
    private EntityManager entityManager; 
    
    @Before
    public void onSetUp() {
        when(entityManager.find(eq(org.gangel.orders.entity.Customer.class), any(Long.class)))
            .thenReturn(org.gangel.orders.entity.Customer.builder().id(OBJECT_ID).build());
    }
    
    @Test
    public void convertProtoToEntity() {
        assertNotNull(customerMapper);
        
        final String VALUE_NAME = "name";
        final String VALUE_LASTNAME = "lastname";
        final String VALUE_EMAIL = "email@domain.com";
        
        Customer customerProto = Customer.newBuilder()
                .setId(OBJECT_ID)
                .setName(VALUE_NAME)
                .setLastname(VALUE_LASTNAME)
                .setEmail(VALUE_EMAIL)
                .build();
        
        
        org.gangel.orders.entity.Customer customerEntity = customerMapper.toEntity(customerProto);
        
        assertEquals(new Long(OBJECT_ID), customerEntity.getId());
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
        customerEntity.setId(OBJECT_ID);
        customerEntity.setName(VALUE_NAME);
        customerEntity.setLastname(VALUE_LASTNAME);
        customerEntity.setEmail(VALUE_EMAIL);
        
        Customer.Builder builder = (Customer.Builder)customerMapper.toProto(customerEntity);
        
        assertNotNull(builder);
        Customer customer = builder.build();
        
        assertEquals(OBJECT_ID, customer.getId());
        assertEquals(VALUE_NAME, customer.getName());
        assertEquals(VALUE_LASTNAME, customer.getLastname());
        assertEquals(VALUE_EMAIL, customer.getEmail());
    }    
}
