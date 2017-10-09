package org.gangel.cloud.dataservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonRootName("order_item")
public class OrderItemTO implements Serializable, DTO<Long> {

    private static final long serialVersionUID = -5296568930869499662L;
    
    private Long id;
    
    private Integer position;
    
    private Integer quantity;
    
    private Double amount;
    
    private Long productId;  

}
