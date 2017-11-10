package org.gangel.orders.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
@Getter @Setter
@EqualsAndHashCode(callSuper=false, of="id")
public class Orders extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    private Customer customer; 
    
//    private int totalQuantity;
//    
//    @Column(nullable=false, columnDefinition="numeric(10,2)")
//    private double totalAmount; 
    
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    @OrderBy("id, lineNumber")
    private SortedSet<OrderItem> orderItems; 
    
}
