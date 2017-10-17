package org.gangel.orders.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Getter @Setter
@EqualsAndHashCode(callSuper=false, of="id")
public class Product extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    @Column(nullable=false)
    private String title;
    
    @Column(nullable=false, length = 8000)    
    private String description;
    
    @Column(nullable=false, columnDefinition="numeric(10,2)")
    private double price;

}
