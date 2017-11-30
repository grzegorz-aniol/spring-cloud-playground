package org.gangel.orders.repository;

import org.gangel.orders.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    @Query("select new java.lang.Long(c.id) from Customer c")
    Stream<Long> getAllIds();
    
    @Query("select new org.gangel.orders.repository.IdsRange(min(c.id), max(c.id)) from Customer c")
    IdsRange getIdsRange();
    
}
