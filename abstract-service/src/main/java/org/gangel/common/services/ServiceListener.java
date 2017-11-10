package org.gangel.common.services;

import java.io.Serializable;

public interface ServiceListener<E extends AbstractEntity<ID>, ID extends Serializable> {

    default void beforeSave(E entity) {} 
    
    default void afterSave(E entity) {}
    
    default void beforeUpdate(E entity) {}
    
    default void afterUpdate(E entity) {} 
    
    default void onGet(E entity) {}
    
    default void beforeDelete(E entity) {}
    
    default void afterDelete(ID id) {}
}
