package org.gangel.cloud.dataservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
@EqualsAndHashCode(of="id")
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    @Column(nullable=false)
    private String name;
    
    @Column(nullable=false)
    private String lastname;
    
    private String phone;
    
    @Column(nullable=false)
    private String email;
}
