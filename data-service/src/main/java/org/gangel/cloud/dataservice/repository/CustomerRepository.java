package org.gangel.cloud.dataservice.repository;

import org.gangel.cloud.dataservice.entity.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}
