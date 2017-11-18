package org.gangel.orders.grpc.mappers;

import com.google.protobuf.Message;
import lombok.SneakyThrows;
import org.gangel.common.services.AbstractEntity;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Abstract implementation for Protobuf - Entity converters ready to be used as a Mapstruct's mapper. See hierarchy of subclasses.  
 * 
 * The class provides an abstraction for conversion between entities and protobuf messages. 
 * In fact it's not simple converter but also a simple repository manager because an existing entity can be fetched from database before updating it. 
 * 
 * In contradiction to entities, protobuf messages are immutable and created by dedicated builder.
 * 
 * @author Grzegorz_Aniol
 *
 * @param <E> Hibernate's entity.
 * @param <T> Google Protobuf message
 * @param <ID> Type of objects identifiers
 */
public abstract class AbstractGrpcMapper<E extends AbstractEntity<ID>, 
        T extends Message, 
        B extends Message.Builder, 
        ID extends Serializable>
            implements GrpcEntityMapper<E, T, B> {

    public abstract ID getIdentifier(T dto);

    @PersistenceContext
    protected EntityManager entityManager;

    @ObjectFactory
    @SneakyThrows
    public E entityFactory(T source, @TargetType Class<E> cls) {
        if (source == null || getIdentifier(source) == null) {
            return cls.newInstance();
        }
        E result = entityManager.find(cls, getIdentifier(source));
        if (result == null) {
            result = cls.newInstance();
        }
        return result;
    }

    public E fetchObject(ID id, @TargetType Class<E> cls) {
        E entity = entityManager.find(cls, id);
        if (entity == null) {
            throw new RuntimeException(
                    String.format("Entity %s not found for ID=%d", cls.getSimpleName(), id));
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public List<T> toProto(List<E> entityList) {
        if (entityList == null) {
            return null;
        }
        return entityList.stream()
                .map( d -> (T)toProto(d).build() )
                .collect(Collectors.toList());       
    }
    
    @SuppressWarnings("unchecked")
    private T safeToProto(E entity) {
        if (entity == null) {
            return null;
        }
        T.Builder b = toProto(entity);
        if (b == null) {
            return null;
        }
        return (T)b.build();
    }

    public Page<T> toDTO(Page<E> entitiesPage) {
        if (entitiesPage == null) {
            return null;
        }
        return entitiesPage.map(this::safeToProto);      
    }

    public List<E> toEntity(List<T> transferList) {
        if (transferList == null) {
            return null;
        }
        return transferList.stream().map(r->toEntity(r)).collect(Collectors.toList());       
    }    

    public SortedSet<E> toEntityAsSortedSet(List<T> transferList) {
        if (transferList == null) {
            return null;
        }
        return transferList.stream().map(r->toEntity(r)).collect(Collectors.toCollection(TreeSet::new));       
    }    
    
}
