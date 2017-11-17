package org.gangel.orders.grpc.mappers;

import com.google.protobuf.Message;
import org.gangel.common.services.AbstractEntity;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Interface for mapping between Hibernate's entity and Google Protobuf object.
 * 
 * In contradiction to entities, protobuf messages are immutable and created by dedicated builder.
 * 
 * @author Grzegorz_Aniol
 *
 * @param <E> Hibernate's entity. In fact an entity that implements @See AbstractEntity interface
 * @param <T> Protobuf object. It implements @See Message interface. 
 */
public interface GrpcEntityMapper<E extends AbstractEntity<? extends Serializable>, T extends Message, B extends Message.Builder> {

    B toProto(E source);
    
    E toEntity(T source);
    
    @SuppressWarnings("unchecked")
    default List<T> toProto(List<E> entityList) {
        if (entityList == null) {
            return null;
        }
        return entityList.stream()
                .map( d -> (T)toProto(d).build() )
                .collect(Collectors.toList());       
    }
//
//    default Page<T> toDTO(Page<E> entitiesPage) {
//        if (entitiesPage == null) {
//            return null;
//        }
//        return entitiesPage.map((c) -> toDTO(c));      
//    }

    default List<E> toEntity(List<T> transferList) {
        if (transferList == null) {
            return null;
        }
        return transferList.stream().map(r->toEntity(r)).collect(Collectors.toList());       
    }    

    default SortedSet<E> toEntityAsSortedSet(List<T> transferList) {
        if (transferList == null) {
            return null;
        }
        return transferList.stream().map(r->toEntity(r)).collect(Collectors.toCollection(TreeSet::new));       
    }    
    
}
