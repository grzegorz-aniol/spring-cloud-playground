package org.gangel.cloud.dataservice.service.mappers;

import org.gangel.cloud.dataservice.dto.OrdersTO;
import org.gangel.orders.entity.Orders;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy=ReportingPolicy.WARN, nullValueCheckStrategy=NullValueCheckStrategy.ALWAYS, 
        uses={OrderItemMapper.class, CustomerMapper.class})
public abstract class OrdersMapper extends AbstractMapper<Orders, OrdersTO, Long>{
    
    @Mappings({
        @Mapping(target="customerId", source="customer.id")
    })
    public abstract OrdersTO toDTO(Orders source);
    
    @Mappings({
        @Mapping(target="customer", source="customerId")
    })
    public abstract Orders toEntity(OrdersTO source);
    
    @AfterMapping
    public void update(final @MappingTarget Orders orders, OrdersTO source) {
        if (orders != null && source != null) {
            if (orders.getOrderItems() != null) {
                orders.getOrderItems().stream().forEach(oi -> oi.setOrder(orders));                
            }
        }
    }

}
