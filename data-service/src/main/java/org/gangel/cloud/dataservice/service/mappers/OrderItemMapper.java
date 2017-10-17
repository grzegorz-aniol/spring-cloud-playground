package org.gangel.cloud.dataservice.service.mappers;

import org.gangel.cloud.dataservice.dto.OrderItemTO;
import org.gangel.orders.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;


@Component
@Mapper(unmappedTargetPolicy=ReportingPolicy.WARN, nullValueCheckStrategy=NullValueCheckStrategy.ALWAYS,
        uses={ProductMapper.class})
public abstract class OrderItemMapper extends AbstractMapper<OrderItem, OrderItemTO, Long> {

    @Mappings({
        @Mapping(target="position", source="lineNumber"),
        @Mapping(target="productId", source="product.id")
    })
    public abstract OrderItemTO toDTO(OrderItem source);

    @Mappings({
        @Mapping(target="lineNumber", source="source.position"),
        @Mapping(target="product", source="source.productId")
    })
    public abstract OrderItem toEntity(OrderItemTO source);
    
}
