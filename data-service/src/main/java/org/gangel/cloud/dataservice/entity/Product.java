package org.gangel.cloud.dataservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Getter @Setter
@EqualsAndHashCode(of="id")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    @Column(nullable=false)
    private String title;
    
    @Column(nullable=false, length = 8000)    
    private String description;
    
    @Column(nullable=false, columnDefinition="numeric(10,2)")
    private double price;
    
    @Column(name = "create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)    
    private Date createDate;
    
    @Column(name = "modification_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)    
    private Date modificationDate;
    
    //@JsonManagedReference("product")
    @OneToMany(cascade=CascadeType.ALL, mappedBy="product")
    private List<OrderItem> orderItems;
    
}
