package org.gangel.orders.grpc.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import lombok.val;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.OrderItem;
import org.gangel.orders.proto.Orders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
@Import(value=MapperConfiguration.class)
public class OrdersMapperTest {

    private static final Long OBJECT_ID = 234L;

    @InjectMocks
    private OrdersMapper ordersMapper = Mappers.getMapper(OrdersMapper.class);

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private CustomerMapper customerMapper;
    
    @Mock
    private EntityManager entityManager;    
    
    @Before
    public void onSetUp() {
        when(entityManager.find(eq(org.gangel.orders.entity.Orders.class), any(Long.class)))
            .thenReturn(org.gangel.orders.entity.Orders.builder()
                        .id(OBJECT_ID)
                        .build());
        when(entityManager.find(eq(org.gangel.orders.entity.Customer.class), any(Long.class)))
            .thenReturn(org.gangel.orders.entity.Customer.builder().id(202L).build());
        
        when(customerMapper.fetchObject(any(Long.class), eq(org.gangel.orders.entity.Customer.class)))
            .thenReturn(org.gangel.orders.entity.Customer.builder().id(202L).build());
        
        when(orderItemMapper.toEntity(any(OrderItem.class))).thenReturn(new org.gangel.orders.entity.OrderItem());
        when(orderItemMapper.toProto(any(org.gangel.orders.entity.OrderItem.class))).thenReturn(OrderItem.newBuilder().setId(456L));
        
        when(customerMapper.toEntity(any(Customer.class))).thenReturn(new org.gangel.orders.entity.Customer());
        when(customerMapper.toProto(any(org.gangel.orders.entity.Customer.class))).thenReturn(Customer.newBuilder().setId(202L));
        
        val ts = new TreeSet<org.gangel.orders.entity.OrderItem>();
        ts.add(org.gangel.orders.entity.OrderItem.builder().build());
        when(orderItemMapper.toEntityAsSortedSet(any())).thenReturn(ts);
    }    
    
    @Test
    public void testProtoToEntityMapper() {
        Orders orders = Orders.newBuilder()
            .setId(OBJECT_ID)
            .setCustomerId(202)
            .addOrderItem(OrderItem.newBuilder().setId(1).build())
            .build();
        
        org.gangel.orders.entity.Orders ordersEntity = ordersMapper.toEntity(orders);
        
        assertEquals(new Long(OBJECT_ID), ordersEntity.getId());
        
        assertNotNull(ordersEntity.getCustomer());
        assertEquals(new Long(202L), ordersEntity.getCustomer().getId());
        
        SortedSet<org.gangel.orders.entity.OrderItem> orderItems = ordersEntity.getOrderItems();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        org.gangel.orders.entity.OrderItem orderItem = orderItems.first();
        assertNotNull(orderItem);
    }
    
    @Test
    public void testEntityToProtoMapper() {
        org.gangel.orders.entity.Orders ordersEntity = org.gangel.orders.entity.Orders.builder()
            .id(OBJECT_ID)
            .customer(org.gangel.orders.entity.Customer.builder()
                    .id(234L)
                    .email("email@domain.com")
                    .name("customer")
                    .build())
            .orderItem(org.gangel.orders.entity.OrderItem.builder()
                    .id(456L)
                    .lineNumber(1)
                    .quantity(2)
                    .amount(456)
                    .build())
            .orderItem(org.gangel.orders.entity.OrderItem.builder()
                    .id(457L)
                    .lineNumber(2)
                    .quantity(1)
                    .amount(123)
                    .build())                    
            .orderItem(org.gangel.orders.entity.OrderItem.builder()
                    .id(458L)
                    .lineNumber(3)
                    .quantity(12)
                    .amount(120)
                    .build())
            .build();
        
        Orders orders = ordersMapper.toProto(ordersEntity).build();
        
        assertNotNull(orders);
        assertEquals(OBJECT_ID.longValue(), orders.getId());
        
        List<OrderItem> orderItemList = orders.getOrderItemList();
        assertNotNull(orderItemList);
        assertEquals(3, orderItemList.size());
    }
    
    @Test
    public void testSimpleEntityToProtoMapper() {
        org.gangel.orders.entity.Orders ordersEntity = org.gangel.orders.entity.Orders.builder()
            .id(OBJECT_ID)
            .customer(org.gangel.orders.entity.Customer.builder()
                    .id(234L)
                    .email("email@domain.com")
                    .name("customer")
                    .build())
            .orderItem(org.gangel.orders.entity.OrderItem.builder()
                    .id(456L)
                    .lineNumber(1)
                    .quantity(2)
                    .amount(456)
                    .build())
            .build();
        
        Orders orders = ordersMapper.toProto(ordersEntity).build();
        
        assertNotNull(orders);
        assertEquals(OBJECT_ID.longValue(), orders.getId());
        
        List<OrderItem> orderItemList = orders.getOrderItemList();
        assertNotNull(orderItemList);
        assertEquals(1, orderItemList.size());
    }    
}
