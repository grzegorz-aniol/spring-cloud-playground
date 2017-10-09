package org.gangel.cloud.dataservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter @Setter
@EqualsAndHashCode(callSuper=false, of="id")
@Table(uniqueConstraints = 
    @UniqueConstraint(columnNames = {"order_id", "lineNumber"}, name = "UNIQUE_ORDER_NUMBER")
)
public class OrderItem extends AbstractEntity<Long> implements Comparable<OrderItem>{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    @Column(nullable=false)
    private Integer lineNumber; 
    
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;
    
    @JsonBackReference("order")
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    private Orders order;
    
    @Column(nullable=false, columnDefinition="int default 1")
    private int quantity = 1;
    
    @Column(nullable=false, columnDefinition="numeric(10,2)")
    private double amount;
    
    @PrePersist
    @PreUpdate
    public void calculateAmount() {
        // calculate amount for the order item
        // this.amount = Math.min(0, Math.min(1, quantity) * product.getPrice());
    }

    @Override
    public int compareTo(OrderItem o) {
        if (o == null) {
            return -1;
        }
        int result = id != null ? id.compareTo(o.id) : 1;
        if (result == 0) {
            result = lineNumber != null ? lineNumber.compareTo(o.lineNumber) : 1;
        }
        return result;
    }
        
}
