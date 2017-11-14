package org.gangel.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
@EqualsAndHashCode(callSuper=false, of="id")
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Customer extends BaseEntity<Long> {

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
