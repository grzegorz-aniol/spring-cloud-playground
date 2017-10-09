package org.gangel.cloud.dataservice.dto;

import java.io.Serializable;

public interface DTO<ID extends Serializable> {

    public ID getId();
    
    public void setId(ID identifier);
}
