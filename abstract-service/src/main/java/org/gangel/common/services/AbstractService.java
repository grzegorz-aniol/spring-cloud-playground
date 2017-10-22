package org.gangel.common.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

public abstract class AbstractService<E extends AbstractEntity<ID>, T extends DTO<ID>, ID extends Serializable> {

    protected abstract PagingAndSortingRepository<E, ID> getRepo();
    
    protected abstract AbstractMapper<E, T, ID> getMapper(); 
    
    @Transactional(readOnly=true)
    public T getById(ID identifier) {
        E entity = getRepo().findOne(identifier);
        if (entity == null) {
            return null;
        }
        return getMapper().toDTO(entity);
    }
    
    @Transactional
    public ID save(T dto) {
        if (dto == null) {
            return null;
        }
        E entity = getMapper().toEntity(dto);
        entity = getRepo().save(entity);
        return (entity != null ? entity.getId() : null);
    }
    
    @Transactional
    public T saveAndGet(T dto) {
        if (dto == null) {
            return null;
        }
        E entity = getMapper().toEntity(dto);
        entity = getRepo().save(entity);
        return getMapper().toDTO(entity);
    }
    
    @Transactional
    public void update(ID id, T dto) {
        if (dto == null) {
            return; 
        }
        dto.setId(id);
        E entity = getMapper().toEntity(dto);
        if (entity == null) {
            throw new EntityNotFoundException("Entity not found");
        }
    }
    
    @Transactional(readOnly=true)
    public Page<T> getAll(Pageable pageable) {
        return getRepo().findAll(pageable).map(e -> getMapper().toDTO(e));
    }
    
}
