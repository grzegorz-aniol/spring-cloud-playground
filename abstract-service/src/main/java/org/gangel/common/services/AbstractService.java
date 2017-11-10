package org.gangel.common.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

public abstract class AbstractService<E extends AbstractEntity<ID>, T extends DTO<ID>, ID extends Serializable> 
    implements ServiceListener<E, ID>
    {

    protected abstract PagingAndSortingRepository<E, ID> getRepo();
    
    protected abstract AbstractMapper<E, T, ID> getMapper(); 
    
    @Transactional(readOnly=true)
    public T getById(ID identifier) {
        E entity = getRepo().findOne(identifier);
        if (entity == null) {
            return null;
        }
        onGet(entity);
        T dto = getMapper().toDTO(entity);
        return dto;
    }
    
    @Transactional
    public ID save(T dto) {
        if (dto == null) {
            return null;
        }
        E entity = getMapper().toEntity(dto);
        beforeSave(entity);
        entity = getRepo().save(entity);
        afterSave(entity);
        return (entity != null ? entity.getId() : null);
    }
    
    @Transactional
    public T saveAndGet(T dto) {
        if (dto == null) {
            return null;
        }
        E entity = getMapper().toEntity(dto);
        beforeSave(entity);
        entity = getRepo().save(entity);
        afterSave(entity);
        return getMapper().toDTO(entity);
    }
    
    @Transactional
    public void update(ID id, T dto) {
        if (dto == null) {
            return; 
        }
        dto.setId(id);      
        beforeUpdate(getRepo().findOne(id));
        E entity = getMapper().toEntity(dto);
        if (entity == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        afterUpdate(entity);
    }
    
    @Transactional(readOnly=true)
    public Page<T> getAll(Pageable pageable) {
        return getRepo().findAll(pageable).map(e -> getMapper().toDTO(e));
    }
    
}
