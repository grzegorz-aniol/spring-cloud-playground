package org.gangel.cloud.dataservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Getter @Setter
@EqualsAndHashCode(callSuper=false, of="id")
@Slf4j
public class Orders extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 
    
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    private Customer customer; 
    
    @JsonManagedReference("order")
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    @OrderBy("id, lineNumber")
    private SortedSet<OrderItem> orderItems; 
    
}
