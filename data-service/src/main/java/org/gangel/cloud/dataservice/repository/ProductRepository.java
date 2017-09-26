package org.gangel.cloud.dataservice.repository;

import org.gangel.cloud.dataservice.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path="/products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
