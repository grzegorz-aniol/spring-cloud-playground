package org.gangel.cloud.dataservice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> extends AbstractEntity<ID> {

    @Column(name = "create_date", updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)    
    private Date createDate;
    
    @Column(name = "modification_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)    
    private Date modificationDate;
    
    @PreUpdate
    protected void updateModificationDate() {
        this.modificationDate = new Date();
    }
    

}
