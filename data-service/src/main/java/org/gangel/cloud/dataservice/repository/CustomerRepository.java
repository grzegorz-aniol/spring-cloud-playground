package org.gangel.cloud.dataservice.repository;

import org.gangel.cloud.dataservice.entity.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path="/customers")
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}
