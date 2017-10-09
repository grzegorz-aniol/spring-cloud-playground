package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.dto.DTO;
import org.gangel.cloud.dataservice.dto.ProductTO;
import org.gangel.cloud.dataservice.entity.AbstractEntity;
import org.gangel.cloud.dataservice.service.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

public abstract class AbstractController<E extends AbstractEntity<ID>, T extends DTO<ID>, ID extends Serializable> {

    protected abstract AbstractService<E, T, ID> getService();
    
    @RequestMapping(method=RequestMethod.GET, path="/{id}")
    public T get(@PathVariable("id") ID id) {
        return getService().getById(id);
    }

    @RequestMapping(method=RequestMethod.PUT, path="/{id}")
    public void update(@PathVariable("id") ID id, @RequestBody T dto) {
        dto.setId(id);
        getService().update(id, dto);
    }    
    
    @RequestMapping(method=RequestMethod.POST) 
    public void addNew(@RequestBody T dto) {
        dto.setId(null);
        getService().save(dto);
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public Page<T> getAll(Pageable pageable) {
        return getService().getAll(pageable);
    }
    
}
