package org.gangel.common.services;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> {

    public abstract ID getId();
}
