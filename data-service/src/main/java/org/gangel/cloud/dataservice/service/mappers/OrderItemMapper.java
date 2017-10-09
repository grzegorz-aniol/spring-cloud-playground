package org.gangel.cloud.dataservice.service.mappers;

import org.gangel.cloud.dataservice.dto.OrderItemTO;
import org.gangel.cloud.dataservice.entity.OrderItem;
import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.TargetType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;


@Component
@Mapper(unmappedTargetPolicy=ReportingPolicy.WARN, nullValueCheckStrategy=NullValueCheckStrategy.ALWAYS,
        uses={ProductMapper.class})
public abstract class OrderItemMapper extends AbstractMapper<OrderItem, OrderItemTO, Long> {

    @Override
    protected Class<OrderItem> getEntityClass() {
        return OrderItem.class;
    }

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
