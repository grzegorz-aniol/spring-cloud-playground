package org.gangel.orders.repository;

import org.gangel.orders.entity.Orders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<Orders, Long> {

}
