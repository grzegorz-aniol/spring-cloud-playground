package org.gangel.orders.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gangel.common.services.DTO;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @Builder 
@NoArgsConstructor @AllArgsConstructor
@JsonRootName("order") 
public class OrdersTO implements Serializable, DTO<Long> {

    private static final long serialVersionUID = 3170312039886244704L;
    
    private Long id; 
    
    private Long customerId;
        
    private List<OrderItemTO> orderItems;
    
}
