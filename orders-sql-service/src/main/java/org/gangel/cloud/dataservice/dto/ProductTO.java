package org.gangel.cloud.dataservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@JsonRootName("product")
public class ProductTO implements DTO<Long>, Serializable {

    private static final long serialVersionUID = -913504105574560030L;

    private Long id; 
    
    private String title;
    
    private String description;
    
    private double price;
    
}
