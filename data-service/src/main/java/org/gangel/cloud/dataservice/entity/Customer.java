package org.gangel.cloud.dataservice.entity;

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
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    private String name;
    
    private String lastname;
    
    private String phone;
    
    private String email;
}
