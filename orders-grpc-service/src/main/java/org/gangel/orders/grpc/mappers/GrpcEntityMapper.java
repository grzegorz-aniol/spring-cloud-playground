package org.gangel.orders.grpc.mappers;

import com.google.protobuf.Message;
import org.gangel.common.services.AbstractEntity;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

/**
 * Interface for mapping between Hibernate's entity and Google Protobuf object.
 * 
 * In contradiction to entities, protobuf messages are immutable and created by dedicated builder.
 * 
 * @author Grzegorz_Aniol
 *
 * @param <E> Hibernate's entity. In fact an entity that implements @See AbstractEntity interface
 * @param <T> Protobuf object. It implements @See Message interface. 
 * @param <TB> Protobuf builder. It's required due to protobuf object are immutable and they're created only via builder
 */
public interface GrpcEntityMapper<E extends AbstractEntity<? extends Serializable>, T extends Message, TB extends Message.Builder> {

    TB toProto(E source);
    
    E toEntity(T source);
    
    List<T> toProto(List<E> entityList);

    Page<T> toDTO(Page<E> entitiesPage);

    List<E> toEntity(List<T> transferList);

    SortedSet<E> toEntityAsSortedSet(List<T> transferList);
    
}
