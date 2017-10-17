package org.gangel.cloud.dataservice.service.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Generated;
import org.gangel.cloud.dataservice.dto.OrderItemTO;
import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.orders.entity.Customer;
import org.gangel.orders.entity.OrderItem;
import org.gangel.orders.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-10-17T14:55:14+0200",
    comments = "version: 1.2.0.CR2, compiler: javac, environment: Java 1.8.0_131 (Oracle Corporation)"
)
@Component
public class OrdersMapperImpl extends OrdersMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public OrdersTO toDTO(Orders source) {
        if ( source == null ) {
            return null;
        }

        OrdersTO ordersTO = new OrdersTO();

        Long id = sourceCustomerId( source );
        if ( id != null ) {
            ordersTO.setCustomerId( id );
        }
        if ( source.getId() != null ) {
            ordersTO.setId( source.getId() );
        }
        List<OrderItemTO> list = orderItemSortedSetToOrderItemTOList( source.getOrderItems() );
        if ( list != null ) {
            ordersTO.setOrderItems( list );
        }
        else {
            ordersTO.setOrderItems( null );
        }

        return ordersTO;
    }

    @Override
    public Orders toEntity(OrdersTO source) {
        if ( source == null ) {
            return null;
        }

        Orders orders = entityFactory( source, Orders.class );

        if ( source.getCustomerId() != null ) {
            orders.setCustomer( customerMapper.fetchObject( source.getCustomerId(), Customer.class ) );
        }
        if ( source.getId() != null ) {
            orders.setId( source.getId() );
        }
        SortedSet<OrderItem> sortedSet = orderItemTOListToOrderItemSortedSet( source.getOrderItems() );
        if ( sortedSet != null ) {
            orders.setOrderItems( sortedSet );
        }
        else {
            orders.setOrderItems( null );
        }

        update( orders, source );

        return orders;
    }

    private Long sourceCustomerId(Orders orders) {
        if ( orders == null ) {
            return null;
        }
        Customer customer = orders.getCustomer();
        if ( customer == null ) {
            return null;
        }
        Long id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<OrderItemTO> orderItemSortedSetToOrderItemTOList(SortedSet<OrderItem> sortedSet) {
        if ( sortedSet == null ) {
            return null;
        }

        List<OrderItemTO> list = new ArrayList<OrderItemTO>( sortedSet.size() );
        for ( OrderItem orderItem : sortedSet ) {
            list.add( orderItemMapper.toDTO( orderItem ) );
        }

        return list;
    }

    protected SortedSet<OrderItem> orderItemTOListToOrderItemSortedSet(List<OrderItemTO> list) {
        if ( list == null ) {
            return null;
        }

        SortedSet<OrderItem> sortedSet = new TreeSet<OrderItem>();
        for ( OrderItemTO orderItemTO : list ) {
            sortedSet.add( orderItemMapper.toEntity( orderItemTO ) );
        }

        return sortedSet;
    }
}
