package org.gangel.cloud.dataservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 

    
    private int lineNumber; 
    
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    private Product product;
    
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    private Orders order;
    
    private int quantity;
    
    private BigInteger orderPrice;    
        
}
