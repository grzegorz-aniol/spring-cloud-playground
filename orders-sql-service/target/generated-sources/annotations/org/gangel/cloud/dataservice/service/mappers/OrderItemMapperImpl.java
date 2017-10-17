package org.gangel.cloud.dataservice.service.mappers;

import javax.annotation.Generated;
import org.gangel.cloud.dataservice.dto.OrderItemTO;
import org.gangel.orders.entity.OrderItem;
import org.gangel.orders.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-10-17T14:55:14+0200",
    comments = "version: 1.2.0.CR2, compiler: javac, environment: Java 1.8.0_131 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl extends OrderItemMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public OrderItemTO toDTO(OrderItem source) {
        if ( source == null ) {
            return null;
        }

        OrderItemTO orderItemTO = new OrderItemTO();

        Long id = sourceProductId( source );
        if ( id != null ) {
            orderItemTO.setProductId( id );
        }
        if ( source.getLineNumber() != null ) {
            orderItemTO.setPosition( source.getLineNumber() );
        }
        if ( source.getId() != null ) {
            orderItemTO.setId( source.getId() );
        }
        orderItemTO.setQuantity( source.getQuantity() );
        orderItemTO.setAmount( source.getAmount() );

        return orderItemTO;
    }

    @Override
    public OrderItem toEntity(OrderItemTO source) {
        if ( source == null ) {
            return null;
        }

        OrderItem orderItem = entityFactory( source, OrderItem.class );

        if ( source.getPosition() != null ) {
            orderItem.setLineNumber( source.getPosition() );
        }
        if ( source.getProductId() != null ) {
            orderItem.setProduct( productMapper.fetchObject( source.getProductId(), Product.class ) );
        }
        if ( source.getId() != null ) {
            orderItem.setId( source.getId() );
        }
        if ( source.getQuantity() != null ) {
            orderItem.setQuantity( source.getQuantity() );
        }
        if ( source.getAmount() != null ) {
            orderItem.setAmount( source.getAmount() );
        }

        return orderItem;
    }

    private Long sourceProductId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
