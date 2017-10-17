package org.gangel.cloud.dataservice.service.mappers;

import lombok.SneakyThrows;
import org.gangel.cloud.dataservice.dto.DTO;
import org.gangel.orders.entity.AbstractEntity;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractMapper<E extends AbstractEntity<ID>, T extends DTO<ID>, ID extends Serializable > 
        implements EntityMapper<E, T> {

    @PersistenceContext
    protected EntityManager entityManager;

    @ObjectFactory
    @SneakyThrows
    public E entityFactory(T source, @TargetType Class<E> cls) {
        if (source == null || source.getId() == null) {
            return cls.newInstance();
        }
        E result = entityManager.find(cls, source.getId());
        if (result == null) {
            result = cls.newInstance();
        }
        return result;
    }
    
    public E fetchObject(ID id, @TargetType Class<E> cls) {        
        E entity = entityManager.find(cls, id);
        if (entity == null) {
            throw new RuntimeException(String.format("Entity %s not found for ID=%d", cls.getSimpleName(), id));
        }
        return entity; 
    }
    
}
