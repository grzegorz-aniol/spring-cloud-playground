package org.gangel.cloud.dataservice.service;

import lombok.val;
import org.gangel.cloud.dataservice.dto.OrderItemTO;
import org.gangel.cloud.dataservice.entity.OrderItem;
import org.gangel.cloud.dataservice.entity.Orders;
import org.gangel.cloud.dataservice.entity.Product;
import org.gangel.cloud.dataservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {
    
    @Autowired
    protected ProductRepository productRepo;
    
    public OrderItem map(OrderItemTO orderItemTO) {
        if (orderItemTO == null) {
                return null;
        }
        Product product = productRepo.findOne(orderItemTO.getProductId());
        
        OrderItem res = new OrderItem();
        res.setAmount(product.getPrice());
        res.setLineNumber(orderItemTO.getPosition());
        res.setQuantity(orderItemTO.getQuantity());
        res.setProduct(product);
        return res;
    }
    
    public OrderItemTO map(OrderItem orderItem) {
        if(orderItem == null) {
            return null;
        }
        return OrderItemTO.builder()
            .id(orderItem.getId())
            .amount(orderItem.getAmount())
            .quantity(orderItem.getQuantity())
            .position(orderItem.getLineNumber())
            .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null )
            .build();            
    }    
    
    public List<OrderItemTO> map(Collection<OrderItem> orderItems) {
        if (orderItems == null) return null;
        return orderItems.stream().map(this::map).collect(Collectors.toList());
    }

    public SortedSet<OrderItem> map(Orders parent, List<OrderItemTO> orderItems) {
        if (orderItems == null) return null;
        return orderItems.stream()
                .map(this::map)
                .peek(o -> o.setOrder(parent))
                .collect(Collectors.toCollection(() -> new TreeSet<OrderItem>()));
    }
    
}
