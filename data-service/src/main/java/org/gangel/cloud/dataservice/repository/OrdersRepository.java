package org.gangel.cloud.dataservice.repository;

import org.gangel.cloud.dataservice.entity.Orders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<Orders, Long> {

}
