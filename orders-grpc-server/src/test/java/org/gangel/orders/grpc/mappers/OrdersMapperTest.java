package org.gangel.orders.grpc.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.gangel.orders.proto.OrderItem;
import org.gangel.orders.proto.Orders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.SortedSet;

@RunWith(SpringRunner.class)
@Import(value=MapperConfiguration.class)
public class OrdersMapperTest {

    @InjectMocks
    private OrdersMapper ordersMapper = OrdersMapper.INSTANCE;
    
    @Mock
    OrdersMapper.BuilderFactory builderFactory;
    
    @Before
    public void onSetUp() {
        when(builderFactory.builder()).thenReturn(Orders.newBuilder());
    }    
    
    @Test
    public void testProtoToEntityMapper() {
        Orders orders = Orders.newBuilder()
            .setId(101)
            .setCustomerId(202)
            .addOrderItem(OrderItem.newBuilder().setAmount(123).setId(1).setProductId(123).setQuantity(2).build())
            .build();
        
        org.gangel.orders.entity.Orders ordersEntity = ordersMapper.map(orders);
        
        assertEquals(new Long(101L), ordersEntity.getId());
        
        assertNotNull(ordersEntity.getCustomer());
        assertEquals(new Long(202L), ordersEntity.getCustomer().getId());
        
        SortedSet<org.gangel.orders.entity.OrderItem> orderItems = ordersEntity.getOrderItems();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        org.gangel.orders.entity.OrderItem orderItem = orderItems.first();
        assertNotNull(orderItem);
        assertNotNull(orderItem.getProduct());
        assertEquals(123.0, orderItem.getAmount(), 1e-5);
        assertEquals(1L, orderItem.getId().longValue());
        assertEquals(2, orderItem.getQuantity());
        
    }
    
    @Test
    public void testEntityToProtoMapper() {
        org.gangel.orders.entity.Orders ordersEntity = org.gangel.orders.entity.Orders.builder()
            .id(123L)
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
        
        Orders orders = ordersMapper.map(ordersEntity).build();
        
        assertNotNull(orders);
        assertEquals(123L, orders.getId());
        
        List<OrderItem> orderItemList = orders.getOrderItemList();
        assertNotNull(orderItemList);
        assertEquals(1, orderItemList.size());
    }
}
