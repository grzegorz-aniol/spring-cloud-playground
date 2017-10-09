package org.gangel.cloud.dataservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@JsonRootName("customer")
public class CustomerTO implements Serializable, DTO<Long> {

    private static final long serialVersionUID = 2014593847764186917L;

    private Long id; 
    
    private String name;
    
    private String lastname;
    
    private String phone;
    
    private String email;    
}
